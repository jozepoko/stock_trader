package jozepoko.stock_trader.stock_price_complementer.domain.service

import jozepoko.stock_trader.core.domain.service.trade.enum.{MarketEnum, Market}

/**
 * 株価データダウンロードサイトで扱われる「市場」の文字列をパースする処理を持つ。
 */
class KDBMarketParser {
  /**
   * 株価データダウンロードサイトで扱われる「市場」の文字列をパースし、Market型に変換する。
   * @param m String型の市場
   * @return Market型の市場
   */
  def parse(m: String): Market = {
    m match {
      case "東証1部" => MarketEnum.TousyouFirst
      case "東証2部" => MarketEnum.TousyouSecond
      case s if s.startsWith("JQ") => MarketEnum.Jasdaq
      case "東証マザーズ" => MarketEnum.Mothers
      case _ => MarketEnum.Other
    }
  }
}
