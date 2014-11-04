package jozepoko.stock_trader.stock_price_complementer.application

import jozepoko.stock_trader.core.domain.entity.Environment
import jozepoko.stock_trader.core.infrastructure.mysql.Connection

object Main {
  def main(args: Array[String]): Unit = {
    val environment = new Environment()
    println(environment.manexLogin.id)
    println(environment.manexLogin.password)
    println(environment.mysql.user)
    println(environment.mysql.password)
    println(environment.mysql.host)
    println(environment.mysql.port)
    Connection
  }
}
