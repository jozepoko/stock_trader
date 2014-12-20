package jozepoko.stock_trader.stock_price_complementer.domain.entity

/**
 * 株価データダウンロードサイトの、分足、5分足の株価を表すエンティティ。
 * idは持たせていないが、とりあえずエンティティにおいておく。
 * TODO リファクタリングしろ
 *
 * @param day  日付
 * @param time 時刻
 * @param openingPrice 始値
 * @param highPrice 高値
 * @param lowPrice 安値
 * @param closingPrice 終値
 * @param turnover 出来高
 * @param salesValue 売買代金
 */
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
