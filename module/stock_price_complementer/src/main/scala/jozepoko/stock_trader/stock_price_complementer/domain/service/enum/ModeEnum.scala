package jozepoko.stock_trader.stock_price_complementer.domain.service.enum

object ModeEnum {
  object Past extends Mode("past")
  object BeforeDay extends Mode("before_day")

  val list = List(Past, BeforeDay)

  def find(name: String): Option[Mode] = list.find(_.name == name)
}

sealed abstract class Mode(val name: String)
