package jozepoko.stock_trader.stock_price_complementer.domain.entity

import jozepoko.stock_trader.stock_price_complementer.domain.service.enum.Mode
import jozepoko.stock_trader.stock_price_complementer.domain.service.enum.ModeEnum.BeforeDay

case class Argument(
  mode: Mode = BeforeDay
)
