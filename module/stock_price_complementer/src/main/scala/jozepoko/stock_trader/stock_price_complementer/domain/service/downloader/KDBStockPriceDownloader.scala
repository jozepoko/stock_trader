package jozepoko.stock_trader.stock_price_complementer.domain.service.downloader

import java.io.File
import jozepoko.stock_trader.core.domain.service.util.html.HtmlParserBuilder
import jozepoko.stock_trader.core.infrastructure.http._
import jozepoko.stock_trader.stock_price_complementer.domain.service.setting.StockPriceComplementerSettings
import jozepoko.stock_trader.stock_price_complementer.domain.value.KDBStockUrl
import org.joda.time.DateTime
import scala.collection.JavaConverters._

class KDBStockPriceDownloader(
  request: Request = new Request,
  htmlParserBuilder: HtmlParserBuilder = new HtmlParserBuilder
) {
  def downloadDailyStockPriceFromKDB(day: DateTime): File = {
    val url = s"http://k-db.com/stocks/${day.toString("yyyy-MM-dd")}?download=csv"
    val dailyStockPriceFilePath = s"${StockPriceComplementerSettings.KDBDailyDataDirectoryPath}/kdb_${day.toString("yyyyMMddHHmmss")}.csv"
    val file = new File(dailyStockPriceFilePath)
    request.url(url).download(file)
  }

  def downloadStockListFromKDB: List[KDBStockUrl] = {
    val url = "http://k-db.com/stocks/"
    val response = request.url(url).get()
    val records = htmlParserBuilder(response.boby).getElementById("maintable").getElementsByTag("tr").asScala.toList
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

  def downloadFiveMinutelyStockPriceFromKDB(originalCode: String, day: DateTime): File = {
    val url = s"http://k-db.com/stocks/$originalCode/5min?date=${day.toString("yyyy-MM-dd")}&download=csv"
    val file = new File(s"${StockPriceComplementerSettings.KDBFiveMinutelyDataDirectoryPath}/kdb_${originalCode}_${day.toString("yyyyMMddHHmmss")}.csv")
    request.url(url).download(file)
  }

  def downloadMinutelyStockPriceFromKDB(originalCode: String, day: DateTime): File = {
    val url = s"http://k-db.com/stocks/$originalCode/minutely?date=${day.toString("yyyy-MM-dd")}&download=csv"
    val file = new File(s"${StockPriceComplementerSettings.KDBMinutelyDataDirectoryPath}/kdb_${originalCode}_${day.toString("yyyyMMddHHmmss")}.csv")
    request.url(url).download(file)
  }
}
