package jozepoko.stock_trader.stock_price_complementer.domain.service.enum

/**
 * sotck_downloaderのモードのEnum。
 */
object ModeEnum {
  object Past extends Mode("past")  //2日以上前の株価を取得するモード
  object BeforeDay extends Mode("before_day")  //前日の株価を取得するモード

  val list = List(Past, BeforeDay)

  def find(name: String): Option[Mode] = list.find(_.name == name)
}

/**
 * sotck_downloaderのモード。
 * @param name 名前
 */
sealed abstract class Mode(val name: String)
