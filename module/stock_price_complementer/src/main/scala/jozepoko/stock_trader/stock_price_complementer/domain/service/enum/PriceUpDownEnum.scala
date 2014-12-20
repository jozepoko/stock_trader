package jozepoko.stock_trader.stock_price_complementer.domain.service.enum

/**
 * Miz企画の株価の「平均上下」のEnum。
 */
object PriceUpDownEnum {
  object Up extends PriceUpDown("↑")
  object Down extends PriceUpDown("↓")
  object Other extends PriceUpDown("-")

  def list = List(Up, Down)

  def find(value: String): Option[PriceUpDown] = list.find(_.value == value)
}

sealed abstract class PriceUpDown(val value: String)
