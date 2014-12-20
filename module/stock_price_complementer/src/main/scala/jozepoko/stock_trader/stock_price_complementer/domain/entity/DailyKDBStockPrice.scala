package jozepoko.stock_trader.stock_price_complementer.domain.entity

import jozepoko.stock_trader.core.domain.service.trade.enum.Market

//コード,市場,銘柄名,業種,始値,高値,安値,終値,出来高,売買代金
case class DailyKDBStockPrice(
  code: Int,
  market: Market,
  name: String,
  businessType: String,
  openingPrice: Double,
  highPrice: Double,
  lowPrice: Double,
  closingPrice: Double,
  turnover: Long,
  salesValue: Long
)
