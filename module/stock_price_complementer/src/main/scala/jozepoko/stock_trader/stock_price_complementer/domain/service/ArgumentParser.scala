package jozepoko.stock_trader.stock_price_complementer.domain.service

import jozepoko.stock_trader.stock_price_complementer.domain.entity.Argument
import jozepoko.stock_trader.stock_price_complementer.domain.service.exception.InvalidArgumentException
import jozepoko.stock_trader.stock_price_complementer.domain.service.setting.StockPriceComplementerSettings
import org.joda.time.format.DateTimeFormat

object ArgumentParser {
  def parse(args: Array[String]):  Argument = {
    val argument = new scopt.OptionParser[Argument]("stock_price_complementer") {
      opt[String]("from") action { (argument, result) =>
        result.copy(from = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(argument).withTime(0, 0, 0, 0))
      } text {
        s"""取得したい株価の最初の日付をyyyyMMddで指定します。
           |指定できる一番古い日時は${StockPriceComplementerSettings.StartDateTime.toString("yyyyMMdd")}です。
           |デフォルトでは上記の一番古い日時となります。
           |※注意！一番古い日時は多分変わります。ダウンロード先のサイトを要チェック。
           |例: --from 20140310
           |""".stripMargin
      }
      opt[String]("to") action { (argument, result) =>
        result.copy(to = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(argument).withTime(0, 0, 0, 0))
      } text {
        s"""取得したい株価の最後の日付をyyyyMMddで指定します。
           |デフォルトでは現在時刻となります。
           |例: --to 2014528
           |""".stripMargin
      }
    }
    argument.parse(args, Argument()).getOrElse(
      throw new InvalidArgumentException("引数が不正です")
    )
  }
}
