package jozepoko.stock_trader.stock_price_complementer.domain.repository.setting

/**
 * 株価データダウンロードサイトからダウンロードしたファイルの設定.
 */
object MizFileSettings {
  val Separator = ","

  val Enclosure = ""

  val Encoding = "SJIS"

  val CodeColumnName = "コード"

  val MarketColumnName = "市場"

  val NameColumnName = "名称"

  val OpeningPriceColumnName = "始値"

  val HighPriceColumnName = "高値"

  val LowPriceColumnName = "安値"

  val ClosingPriceColumnNanme = "終値"

  val TurnoverColumnName = "出来高"
  
  val ShareUnitNumberColumnName = "単元株数"
  
  val ShareUnitSalesValueColumnName = "単元出来高"

  val PERColumnName = "PER"

  val PBRColumnName = "PBR"

  val FiveDaysPriceAverage = "5日平均"

  val TwentyFiveDaysPriceAverage = "25日平均"

  val SeventyFiveDaysPriceAverage = "75日平均"

  val fiveDaysPriceAverageUpDown = "5日平均上下"

  val TwentyFiveDaysPriceAverageUpDown = "25日平均上下"

  val SeventyFiveDaysPriceAverageUpDown = "75日平均上下"

  val SalesValueChangeRate = "出来高増減率"

  val SalesValueFromeFiveDaysBeforeChangeRate = "5日前からの上下率"

  val BeforeDayClosingPrice = "前日終値"
}
