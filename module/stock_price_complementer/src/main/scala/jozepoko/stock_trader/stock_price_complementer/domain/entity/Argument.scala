package jozepoko.stock_trader.stock_price_complementer.domain.entity

import jozepoko.stock_trader.stock_price_complementer.domain.service.setting.StockPriceComplementerSettings
import org.joda.time.DateTime

case class Argument(
  from: DateTime = StockPriceComplementerSettings.StartDateTime,
  to: DateTime = StockPriceComplementerSettings.EndDateTime
)
