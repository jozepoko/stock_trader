package jozepoko.stock_trader.stock_price_complementer.domain.service.downloader.setting

/**
 * 株価データダウンロードサイトからファイルをダウンロード際の設定。
 */
object KDBStockPriceDownloaderSettings {
  val BaseUrl = "http://k-db.com/stocks/"

  val DownloadParameter = "download=csv"

  val FiveMinutelyUrl = "5min"

  val MinutelyUrl = "minutely"

  val DateParameter = "date="

  val StockListTag = "maintable"
}
