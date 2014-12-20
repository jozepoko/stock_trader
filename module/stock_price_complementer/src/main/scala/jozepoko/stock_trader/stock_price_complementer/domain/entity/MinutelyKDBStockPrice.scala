package jozepoko.stock_trader.stock_price_complementer.domain.entity

//日付,時刻,始値,高値,安値,終値,出来高,売買代金
case class MinutelyKDBStockPrice(
  day: String,
  time: String,
  openingPrice: Double,
  highPrice: Double,
  lowPrice: Double,
  closingPrice: Double,
  turnover: Long,
  salesValue: Long
)
