package jozepoko.stock_trader.stock_price_complementer.domain.service

import jozepoko.stock_trader.core.domain.repository.dao.DailyStockPriceDao
import jozepoko.stock_trader.core.domain.service.util.datetime._
import jozepoko.stock_trader.core.infrastructure.mysql.Connection
import jozepoko.stock_trader.stock_price_complementer.domain.repository.dao.ComplementCompletedDao
import jozepoko.stock_trader.stock_price_complementer.domain.service.downloader.StockPriceDownloader
import org.joda.time.DateTime
import scala.util.control.NonFatal
import scalikejdbc.DBConnection

class PastComplementer(
  downloadDayListGenerator: DownloadDayListGenerator = new DownloadDayListGenerator,
  stockPriceDownloader: StockPriceDownloader = new StockPriceDownloader,
  connection: DBConnection = Connection.connection,
  dailyStockPriceDao: DailyStockPriceDao = new DailyStockPriceDao,
  complementCompletedDao: ComplementCompletedDao = new ComplementCompletedDao
) {
  def complementPast(): (String, String) = {
    val dayList = downloadDayListGenerator.generate
    val firstDay = dayList.head
    val title = "日足株価補完バッチ実行結果通知" + s"${firstDay.formatToJapanese} ～ ${dayList.last.formatToJapanese}"
    val failedDays = dayList.map(downloadDailyStockPrice).collect { case Some(e) => e }
    try {
      complementCompletedDao.truncate
      complementCompletedDao.replace(dayList.last.plusDays(1))
      (
        title,
        s"""$title の株価を補完しようとしましたが、以下の日は飛ばしました
           |
           |飛ばした日:原因 : ${formatFailedDaysInfo(failedDays)}
         """.stripMargin
      )
    } catch {
      case NonFatal(e) =>
        (
          title + " (異常終了)",
          s"""$title の株価補完をしようとしましたが失敗しました。
           |どの日まで補完が完了したかを保存するcomplement_completedテーブルの更新に失敗しました。
           |
           |飛ばした日:原因 : ${formatFailedDaysInfo(failedDays)}
           |
           |エラー内容 : ${e.getMessage}
         """.stripMargin
          )
    }
  }

  private[this] def downloadDailyStockPrice(day: DateTime): Option[(DateTime, String)] = {
    try {
      val stockPrices = stockPriceDownloader.downloadDailyStockPrice(day)
      stockPrices.grouped(1000).map { groupedStockPrices =>
        connection localTx { implicit session =>
          dailyStockPriceDao.replaces(groupedStockPrices)
        }
        Thread.sleep(1000 * 1)  // sqlの実行を1秒待つ
      }
      Thread.sleep(1000 * 30)  // 株価のダウンロードを30秒待つ
      None
    } catch {
      case NonFatal(e) => Some(day, e.getMessage)
    }
  }

  private[this] def formatFailedDaysInfo(failedDays: List[(DateTime, String)]): String = {
    failedDays match {
      case Nil => "なし"
      case l => l.map { case (k, v) => s"${k.formatToJapanese}:$v"} mkString ", "
    }
  }
}
