package jozepoko.stock_trader.stock_price_complementer.domain.service

import jozepoko.stock_trader.core.domain.repository.dao.{MinutelyStockPriceDao, FiveMinutelyStockPriceDao, DailyStockPriceDao}
import jozepoko.stock_trader.core.domain.service.util.datetime._
import jozepoko.stock_trader.core.domain.service.util.mail.MailSender
import jozepoko.stock_trader.core.infrastructure.http.Request
import jozepoko.stock_trader.core.infrastructure.mysql.Connection
import jozepoko.stock_trader.stock_price_complementer.domain.repository.KDBFileReader
import jozepoko.stock_trader.stock_price_complementer.domain.repository.dao.ComplementCompletedDao
import jozepoko.stock_trader.stock_price_complementer.domain.service.downloader.StockPriceDownloader
import jozepoko.stock_trader.stock_price_complementer.domain.service.enum.ModeEnum.{BeforeDay, Past}
import org.joda.time.DateTime
import scala.util.control.NonFatal
import scalikejdbc.DBConnection

class StockPriceComplementer(
  stockPriceDownloader: StockPriceDownloader = new StockPriceDownloader,
  dailyStockPriceDao: DailyStockPriceDao = new DailyStockPriceDao,
  fiveMinutelyStockPriceDao: FiveMinutelyStockPriceDao = new FiveMinutelyStockPriceDao,
  minutelyStockPriceDao: MinutelyStockPriceDao = new MinutelyStockPriceDao,
  connection: DBConnection = Connection.connection,
  downloadDayListGenerator: DownloadDayListGenerator = new DownloadDayListGenerator,
  complementCompletedDao: ComplementCompletedDao = new ComplementCompletedDao,
  kdbFileReader: KDBFileReader = new KDBFileReader,
  request: Request = new Request,
  mailSender: MailSender = new MailSender
) {
  def run(args: Array[String]): Unit = {
    val argument = ArgumentParser.parse(args)
    val (subject, message) = argument.mode match {
      case Past => complementPast()
      case BeforeDay => complementBeforeDay()
    }
    mailSender.subject(subject).message(message).sendMail()
  }

  private[this] def complementPast(): (String, String) = {
    def dayTermMessageBuilder(firstDay: DateTime, lastDay: DateTime): String = {
      s"${firstDay.formatToJapanese} ～ ${lastDay.formatToJapanese}"
    }

    val dayList = downloadDayListGenerator.generate
    val firstDay = dayList.head
    var completedDay = firstDay.minusDays(1)  // 正常に処理が終了した日をcompletedDayとする。最初はとりあえず対象の1日前。

    val title = "日足株価補完バッチ実行結果通知" + dayTermMessageBuilder(firstDay, dayList.last)

    try {
      dayList foreach { day =>
        val stockPrices = stockPriceDownloader.downloadDailyStockPrice(day)
        stockPrices.grouped(1000).map { groupedStockPrices =>
          connection localTx { implicit session =>
            dailyStockPriceDao.replaces(groupedStockPrices)
          }
          Thread.sleep(1000 * 1)  // sqlの実行を1秒待つ
        }
        Thread.sleep(1000 * 30)  // 株価のダウンロードを30秒待つ
        completedDay = day
      }
      complementCompletedDao.truncate
      complementCompletedDao.replace(dayList.last)
      (title + " (正常終了)", title + "の株価補完が完了しました。")
    } catch {
      case NonFatal(e) =>
        val isOneDaySuccess = (firstDay isBefore completedDay) || (firstDay isEqual completedDay)
        if (isOneDaySuccess) {
          complementCompletedDao.truncate
          complementCompletedDao.replace(completedDay)
        }
        val successDay = if (isOneDaySuccess) dayTermMessageBuilder(firstDay, completedDay) else "なし"
        (
          title + " (異常終了)",
          s"""$title の株価補完をしようとしましたが失敗しました。
             |
             |成功した日 : $successDay
             |失敗した日 : ${dayTermMessageBuilder(completedDay, dayList.last)}
             |
             |エラー内容 : ${e.getMessage}
             |
             |失敗した日の分は翌日に実行されます。
           """.stripMargin
        )
    }
  }

  private[this] def complementBeforeDay(): (String, String) = {
    def complementFiveMintutes(yesterDay: DateTime): Either[String, String] = {
      try {
        stockPriceDownloader.downloadFiveMinutelyStockPrice(yesterDay) foreach { fiveMinutelyEntityList =>
          connection localTx { implicit session =>
            fiveMinutelyStockPriceDao.replaces(fiveMinutelyEntityList)
          }
        }
        Right(s"${yesterDay.formatToJapanese} の5分足の株価の補完が完了しました。")
      } catch {
        case NonFatal(e) => Left(s"${yesterDay.formatToJapanese} の5分足の株価の補完に失敗しました。今日中に実行しなおしてください。")
      }
    }

    def complementMinutes(yesterDay: DateTime): Either[String, String] = {
      try {
        stockPriceDownloader.downloadMinutelyStockPrice(yesterDay) foreach { minutelyEntityList =>
          connection localTx { implicit session =>
            minutelyStockPriceDao.replaces(minutelyEntityList)
          }
        }
        Right(s"${yesterDay.formatToJapanese} の分足の株価の補完が完了しました。")
      } catch {
        case NonFatal(e) => Left(s"${yesterDay.formatToJapanese} の分足の株価の補完に失敗しました。今日中に実行しなおしてください。")
      }
    }

    val yesterDay = DateTime.now.minusDays(1).withTime(0, 0, 0, 0)
    if (yesterDay.isTradableDateTime) {
      complementFiveMintutes(yesterDay) match {
        case Right(fiveMinutesResult) =>
          complementMinutes(yesterDay) match {
            case Right(minuteResult) =>
              (s"分足株価補完バッチ実行結果通知 ${yesterDay.formatToJapanese} (正常終了)", s"$fiveMinutesResult\n$minuteResult")
            case Left(minuteError) =>
              (s"分足株価補完バッチ実行結果通知 ${yesterDay.formatToJapanese} (異常終了)", s"$fiveMinutesResult\n$minuteError")
          }
        case Left(fiveMinutesError) =>
          complementMinutes(yesterDay) match {
            case Right(minuteResult) =>
              (s"分足株価補完バッチ実行結果通知 ${yesterDay.formatToJapanese} (異常終了)", s"$fiveMinutesError\n$minuteResult")
            case Left(minuteError) =>
              (s"分足株価補完バッチ実行結果通知 ${yesterDay.formatToJapanese} (異常終了)", s"$fiveMinutesError\n$minuteError")
          }
      }
    } else (s"分足株価補完バッチ実行結果通知 ${yesterDay.formatToJapanese} (正常終了)", s"${yesterDay.formatToJapanese} は株価取引可能日ではないため、分足株価補完はおこないませんでした。")
  }
}
