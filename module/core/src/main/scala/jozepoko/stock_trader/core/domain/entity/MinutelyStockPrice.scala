package jozepoko.stock_trader.core.domain.entity

import jozepoko.stock_trader.core.domain.service.enum.Market
import org.joda.time.DateTime

case class MinutelyStockPrice(
  datetime: DateTime,
  code: Int,
  market: Market,
  name: String,
  openingPrice: Int,
  highPrice: Int,
  lowPrice: Int,
  closingPrice: Int
) extends StockPrice
