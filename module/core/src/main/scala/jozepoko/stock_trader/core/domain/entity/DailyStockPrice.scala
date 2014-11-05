package jozepoko.stock_trader.core.domain.entity

import jozepoko.stock_trader.core.domain.service.enum.Market
import org.joda.time.DateTime

case class DailyStockPrice(
  datetime: DateTime,
  code: Int,
  market: Market,
  name: String,
  openingPrice: Int,
  highPrice: Int,
  lowPrice: Int,
  closingPrice: Int,
  per: Double,
  pbr: Double,
  fiveDaysAverage: Double,
  twentyFiveDaysAverage: Double,
  seventyFiveDaysAverage: Double
) extends StockPrice
