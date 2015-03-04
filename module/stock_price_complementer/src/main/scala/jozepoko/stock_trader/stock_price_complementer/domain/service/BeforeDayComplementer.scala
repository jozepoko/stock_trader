package jozepoko.stock_trader.stock_price_complementer.domain.service

import jozepoko.stock_trader.core.domain.service.util.datetime._
import jozepoko.stock_trader.core.domain.service.util.exception._
import jozepoko.stock_trader.core.domain.repository.dao.{MinutelyStockPriceDao, FiveMinutelyStockPriceDao}
import jozepoko.stock_trader.core.infrastructure.mysql.MixInStockConnectionPool
import jozepoko.stock_trader.stock_price_complementer.domain.service.downloader.StockPriceDownloader
import org.joda.time.DateTime
import scala.util.control.NonFatal

class BeforeDayComplementer(
  stockPriceDownloader: StockPriceDownloader = new StockPriceDownloader,
  fiveMinutelyStockPriceDao: FiveMinutelyStockPriceDao = new FiveMinutelyStockPriceDao,
  minutelyStockPriceDao: MinutelyStockPriceDao = new MinutelyStockPriceDao
) extends MixInStockConnectionPool {
  def complementBeforeDay(): (String, String) = {
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

  private[this] def complementFiveMintutes(yesterDay: DateTime): Either[String, String] = {
    complement(yesterDay, "5分足", {
      stockPriceDownloader.downloadFiveMinutelyStockPrice(yesterDay) foreach { fiveMinutelyEntityList =>
        stockConnectionPool.borrow localTx { implicit session =>
          fiveMinutelyStockPriceDao.replaces(fiveMinutelyEntityList)
        }
      }
    })
  }

  private[this] def complementMinutes(yesterDay: DateTime): Either[String, String] = {
    complement(yesterDay, "分足" , {
      stockPriceDownloader.downloadMinutelyStockPrice(yesterDay) foreach { minutelyEntityList =>
        stockConnectionPool.borrow localTx { implicit session =>
          minutelyStockPriceDao.replaces(minutelyEntityList)
        }
      }
    })
  }

  private[this] def complement(yesterDay: DateTime, name: String, download: => Unit): Either[String, String] = {
    try {
      download
      Right(s"${yesterDay.formatToJapanese} の${name}の株価の補完が完了しました。")
    } catch {
      case NonFatal(e) =>
        println(e.getMessage)
        println(e.getStackTrace)
        Left(
        s"""yesterDay.formatToJapanese} の${name}の株価の補完に失敗しました。今日中に実行しなおしてください。
             |原因 : ${e.getMessage}
             |ローカライズメッセージ : ${e.getLocalizedMessage}
             |スタックトレース :
             |${getStackTraceString(e)}
           """.stripMargin
      )
    }
  }
}
