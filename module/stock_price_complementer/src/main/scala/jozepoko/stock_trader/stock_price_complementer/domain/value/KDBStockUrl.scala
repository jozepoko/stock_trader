package jozepoko.stock_trader.stock_price_complementer.domain.value

/**
 * 株価データダウンロードサイトから株価をダウンロードする際のurlの情報
 * @param originalCode 株価データダウンロードサイトで扱っている株コード
 * @param code 株コード
 * @param name 銘柄名
 * @param market 市場
 */
case class KDBStockUrl(
  originalCode: String,
  code: Int,
  name: String,
  market: String
)
