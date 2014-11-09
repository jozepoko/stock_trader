package jozepoko.stock_trader.core.domain.service.trade.enum

object MarketEnum {
  case object TousyouIchibu extends Market(1, "東証一部")

  val list = List(TousyouIchibu)

  def find(id: Int): Option[Market] = list.find(_.id == id)
}

sealed abstract class Market(val id: Int, val name: String)
