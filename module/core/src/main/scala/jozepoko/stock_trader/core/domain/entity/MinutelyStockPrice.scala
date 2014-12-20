package jozepoko.stock_trader.core.domain.entity

import jozepoko.stock_trader.core.domain.service.trade.enum.Market
import org.joda.time.DateTime

case class MinutelyStockPrice(
  datetime: DateTime,
  code: Int,
  market: Market,
  name: String,
  openingPrice: Double,
  highPrice: Double,
  lowPrice: Double,
  closingPrice: Double
) extends StockPrice
