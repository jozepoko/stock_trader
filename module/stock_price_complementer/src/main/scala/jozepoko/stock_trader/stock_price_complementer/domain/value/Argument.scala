package jozepoko.stock_trader.stock_price_complementer.domain.value

import jozepoko.stock_trader.stock_price_complementer.domain.service.enum.Mode
import jozepoko.stock_trader.stock_price_complementer.domain.service.enum.ModeEnum.BeforeDay

/**
 * 引数の値を持つ値オブジェクト
 * @param mode 実行モード
 */
case class Argument(
  mode: Mode = BeforeDay
)
