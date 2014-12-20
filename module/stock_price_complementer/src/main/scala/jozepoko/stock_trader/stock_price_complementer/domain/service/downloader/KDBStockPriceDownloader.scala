package jozepoko.stock_trader.stock_price_complementer.domain.service.downloader

import java.io.File
import jozepoko.stock_trader.core.domain.service.util.html.HtmlParserBuilder
import jozepoko.stock_trader.core.infrastructure.http._
import jozepoko.stock_trader.stock_price_complementer.domain.service.downloader.setting.KDBStockPriceDownloaderSettings._
import jozepoko.stock_trader.stock_price_complementer.domain.service.setting.StockPriceComplementerSettings
import jozepoko.stock_trader.stock_price_complementer.domain.value.KDBStockUrl
import org.joda.time.DateTime
import scala.collection.JavaConverters._

/**
 * 株価データダウンロードサイトからファイルをダウンロードする処理を持つ。
 */
class KDBStockPriceDownloader(
  request: Request = new Request,
  htmlParserBuilder: HtmlParserBuilder = new HtmlParserBuilder
) {
  /**
   * 指定した日の日足の株価一覧をダウンロードする。
   * @param day 日
   * @return ダウンロードしたファイル
   */
  def downloadDailyStockPriceFromKDB(day: DateTime): File = {
    val dailyStockPriceFilePath = s"${StockPriceComplementerSettings.KDBDailyDataDirectoryPath}/kdb_${day.toString("yyyyMMddHHmmss")}.csv"
    val file = new File(dailyStockPriceFilePath)
    request.url(createDailyUrl(day)).download(file)
  }

  /**
   * 株価データダウンロードサイトが扱う銘柄の一覧を、株価データダウンロードのhtmlを解析して取得する。
   * @return 銘柄一覧
   */
  def downloadStockListFromKDB: List[KDBStockUrl] = {
    val response = request.url(BaseUrl).get()
    val records = htmlParserBuilder(response.boby).getElementById(StockListTag).getElementsByTag("tr").asScala.toList
    records.map { record =>
      val first = record.children.first
      val link = first.select("a[href]").attr("href")
      val originalCode = link.drop(8)
      val code = link.substring(8, 12).toInt
      val name = first.text.drop(code.toString.length + 1)
      val market = record.children.get(2).text
      KDBStockUrl(originalCode, code, name, market)
    }
  }

  /**
   * 指定した銘柄、日の5分足の株価をダウンロードする。
   * @param originalCode 株価データダウンロードサイトで扱う株コード
   * @param day 日
   * @return ダウンロードしたファイル
   */
  def downloadFiveMinutelyStockPriceFromKDB(originalCode: String, day: DateTime): File = {
    val file = new File(s"${StockPriceComplementerSettings.KDBFiveMinutelyDataDirectoryPath}/kdb_${originalCode}_${day.toString("yyyyMMddHHmmss")}.csv")
    request.url(createFiveMinutelyUrl(originalCode, day)).download(file)
  }

  /**
   * 指定した銘柄、日の分足の株価をダウンロードする。
   * @param originalCode 株価データダウンロードサイトで扱う株コード
   * @param day 日
   * @return ダウンロードしたファイル
   */
  def downloadMinutelyStockPriceFromKDB(originalCode: String, day: DateTime): File = {
    val file = new File(s"${StockPriceComplementerSettings.KDBMinutelyDataDirectoryPath}/kdb_${originalCode}_${day.toString("yyyyMMddHHmmss")}.csv")
    request.url(createMinutelyUrl(originalCode, day)).download(file)
  }

  /**
   * 日足の株価をダウンロードするurlを作る。
   * @param day 日
   * @return url
   */
  private def createDailyUrl(day: DateTime): String = {
    s"$BaseUrl${day.toString("yyyy-MM-dd")}&$DownloadParameter"
  }

  /**
   * 5分足の株価をダウンロードするurlを作る。
   * @param originalCode 株価データダウンロードサイトで扱う株コード
   * @param day 日
   * @return url
   */
  private def createFiveMinutelyUrl(originalCode: String, day: DateTime): String = {
    s"$BaseUrl$originalCode/$FiveMinutelyUrl?$DateParameter${day.toString("yyyy-MM-dd")}&$DownloadParameter"
  }

  /**
   * 分足の株価をダウンロードするurlを作る。
   * @param originalCode 株価データダウンロードサイトで扱う株コード
   * @param day 日
   * @return url
   */
  private def createMinutelyUrl(originalCode: String, day: DateTime): String = {
    s"$BaseUrl$originalCode/$MinutelyUrl?$DateParameter${day.toString("yyyy-MM-dd")}&$DownloadParameter"
  }
}
