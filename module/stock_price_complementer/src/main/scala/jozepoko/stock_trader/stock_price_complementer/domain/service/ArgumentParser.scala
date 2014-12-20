package jozepoko.stock_trader.stock_price_complementer.domain.service

import jozepoko.stock_trader.stock_price_complementer.domain.service.enum.ModeEnum
import jozepoko.stock_trader.stock_price_complementer.domain.service.exception.InvalidArgumentException
import jozepoko.stock_trader.stock_price_complementer.domain.service.setting.StockPriceComplementerSettings
import jozepoko.stock_trader.stock_price_complementer.domain.value.Argument
import org.joda.time.format.DateTimeFormat

object ArgumentParser {
  def parse(args: Array[String]):  Argument = {
    val argument = new scopt.OptionParser[Argument]("stock_price_complementer") {
      opt[String]("mode") action { (argument, result) =>
        result.copy(mode = {ModeEnum.find(argument) match {
            case Some(v) => v
            case None => throw new Exception("引数が不正です")
          }
        })
      } text {
        s"""--mode past  or  --mode before_day  デフォルトはbefore_day"""
      }
    }
    argument.parse(args, Argument()).getOrElse(
      throw new InvalidArgumentException("引数が不正です")
    )
  }
}
