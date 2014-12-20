package jozepoko.stock_trader.stock_price_complementer.domain.service.downloader

import java.io.File
import jozepoko.stock_trader.core.infrastructure.http._
import jozepoko.stock_trader.stock_price_complementer.domain.service.setting.StockPriceComplementerSettings
import org.joda.time.DateTime

/**
 * Miz企画から株価をダウンロードする処理を持つ。
 */
class MizStockPriceDownloader(
  request: Request = new Request
) {
  /**
   * Miz企画から日足の株価をダウンロードする処理を持つ。
   * @param day 日
   * @return ダウンロードしたファイル
   */
  def downloadDailyStockPriceFromMiz(day: DateTime): File = {
    val file = new File(filePath(day))
    request.url(createDailyMizUrl(day)).download(file)
  }

  /**
   * 日足の株価をダウンロードするurlを作る。
   * @param day 日
   * @return url
   */
  private def createDailyMizUrl(day: DateTime): String = {
    val year = day.toString("yyyy").substring(2)
    s"http://mizkikaku.web.fc2.com/data/kabu$year${day.toString("MMdd")}.zip"
  }

  /**
   * ダウンロードしたファイルを置くパスを作る。
   * @param day 日
   * @return パス
   */
  private def filePath(day: DateTime): String = {
    s"${StockPriceComplementerSettings.MizDataDirectoryPath}/miz_${day.toString("yyyyMMddHHmmss")}.zip"
  }
}
