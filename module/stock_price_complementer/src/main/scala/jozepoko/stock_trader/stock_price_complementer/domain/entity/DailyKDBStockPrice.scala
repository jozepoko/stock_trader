package jozepoko.stock_trader.stock_price_complementer.domain.entity

import jozepoko.stock_trader.core.domain.service.trade.enum.Market

/**
 * 株価データダウンロードサイトの、日毎の株価を表すエンティティ。
 * idはcode。
 * @param code 株コード
 * @param market 市場
 * @param name 銘柄名
 * @param businessType 業種
 * @param openingPrice 始値
 * @param highPrice 高値
 * @param lowPrice 安値
 * @param closingPrice 終値
 * @param turnover 出来高
 * @param salesValue 売買代金
 */
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
