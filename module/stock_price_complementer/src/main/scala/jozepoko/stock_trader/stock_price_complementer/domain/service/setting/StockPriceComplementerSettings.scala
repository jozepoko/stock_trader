package jozepoko.stock_trader.stock_price_complementer.domain.service.setting

import org.joda.time.DateTime

trait UsesStockPriceComplementerSettings {
  def stockPriceComplementerSettings: StockPriceComplementerSettings
}

trait MixInStockPriceComplementerSettings {
  val stockPriceComplementerSettings: StockPriceComplementerSettings = new StockPriceComplementerSettings
}

class StockPriceComplementerSettings {
  val StartDateTime: DateTime = new DateTime(2011, 5, 2, 0, 0, 0, 0)

  val EndDateTime: DateTime = DateTime.now.withTime(0, 0, 0, 0)

  val DataPath = "data/stock_price_complementer"

  val KDBDataDirectoryPath = s"$DataPath/kdb"

  val KDBDailyDataDirectoryPath = s"$DataPath/kdb/daily"

  val KDBFiveMinutelyDataDirectoryPath = s"$DataPath/kdb/five_minutely"

  val KDBMinutelyDataDirectoryPath = s"$DataPath/kdb/minutely"

  val MizDataDirectoryPath = s"$DataPath/miz"
}
