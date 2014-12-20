package jozepoko.stock_trader.core.domain.entity

import jozepoko.stock_trader.core.domain.service.trade.enum.Market
import org.joda.time.DateTime

trait StockPrice {
  val datetime: DateTime
  val code: Int
  val market: Market
  val name: String
  val openingPrice: Double
  val highPrice: Double
  val lowPrice: Double
  val closingPrice: Double
}
