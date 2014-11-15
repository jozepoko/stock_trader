package jozepoko.stock_trader.core.domain.service.trade.enum

object MarketEnum {
  case object TousyouFirst extends Market(1, "東証一部")
  case object TousyouSecond extends Market(2, "東証二部")
  case object Jasdaq extends Market(3, "ジャスダック")
  case object Mothers extends Market(4, "東証マザーズ")
  case object Other extends Market(99, "その他")

  val list = List(TousyouFirst, TousyouSecond, Jasdaq, Mothers)

  val listWithOther = List(TousyouFirst, TousyouSecond, Jasdaq, Mothers, Other)

  def get(id: Int): Market = list.find(_.id == id).getOrElse(Other)

  def find(id: Int): Option[Market] = list.find(_.id == id)
}

sealed abstract class Market(val id: Int, val name: String)
