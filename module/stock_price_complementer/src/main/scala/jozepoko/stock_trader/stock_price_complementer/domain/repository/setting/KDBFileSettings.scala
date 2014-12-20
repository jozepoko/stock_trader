package jozepoko.stock_trader.stock_price_complementer.domain.repository.setting

/**
 * 株価データダウンロードサイトからダウンロードしたファイルの設定.
 */
object KDBFileSettings {
  val Separator = ","

  val Enclosure = ""

  val Encoding = "SJIS"

  val HeaderStartLine = 2

  val CodeLength = 4

  val CodeColumnName = "コード"

  val MarketColumnName = "市場"

  val NameColumnName = "銘柄名"

  val BusinessTypeColumnName = "業種"

  val OpeningPriceColumnName = "始値"

  val HighPriceColumnName = "高値"

  val LowPriceColumnName = "安値"

  val ClosingPriceColumnNanme = "終値"

  val TurnoverColumnName = "出来高"

  val SalesValueColumnName = "売買代金"

  val DayColumnName = "日付"

  val TimeColumnName = "時刻"
}
