package jozepoko.stock_trader.stock_price_complementer.domain.service.setting

import org.joda.time.DateTime

object StockPriceComplementerSettings {
  val StartDateTime: DateTime = new DateTime(2011, 5, 2, 0, 0, 0, 0)

  val EndDateTime: DateTime = DateTime.now.withTime(0, 0, 0, 0)
}
