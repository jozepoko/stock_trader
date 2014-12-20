package jozepoko.stock_trader.stock_price_complementer.domain.entity

import jozepoko.stock_trader.core.domain.service.trade.enum.Market
import jozepoko.stock_trader.stock_price_complementer.domain.service.enum.PriceUpDown

/**
 * @param code コード
 * @param market 市場
 * @param name 名称
 * @param openingPrice 始値
 * @param highPrice 高値
 * @param lowPrice 安値
 * @param closingPrice 終値
 * @param turnover 出来高
 * @param shareUnitNumber 単元株数
 * @param shareUnitSalesValue 単元出来高
 * @param per PER
 * @param pbr PBR
 * @param fiveDaysPriceAverage 5日平均
 * @param twentyFiveDaysPriceAverage 25日平均
 * @param seventyFiveDaysPriceAverage 75日平均
 * @param fiveDaysPriceAverageUpDown 5日平均上下
 * @param twentyFiveDaysPriceAverageUpDown 25日平均上下
 * @param seventyFiveDaysPriceAverageUpDown 75日平均上下
 * @param salesValueChangeRate 出来高増減率
 * @param salesValueFromeFiveDaysBeforeChangeRate 5日前からの上下率
 * @param beforeDayClosingPrice 前日終値
 */
case class DailyMizStockPrice(
  code: Int,
  market: Market,
  name: String,
  openingPrice: Double,
  highPrice: Double,
  lowPrice: Double,
  closingPrice: Double,
  turnover: Long,
  shareUnitNumber: Int,
  shareUnitSalesValue: Int,
  per: Double,
  pbr: Double,
  fiveDaysPriceAverage: Double,
  twentyFiveDaysPriceAverage: Double,
  seventyFiveDaysPriceAverage: Double,
  fiveDaysPriceAverageUpDown: PriceUpDown,
  twentyFiveDaysPriceAverageUpDown: PriceUpDown,
  seventyFiveDaysPriceAverageUpDown: PriceUpDown,
  salesValueChangeRate: Double,
  salesValueFromeFiveDaysBeforeChangeRate: Double,
  beforeDayClosingPrice: Double
)
