package jozepoko.stock_trader.stock_price_complementer.application

import jozepoko.stock_trader.core.infrastructure.environment.Environment
import jozepoko.stock_trader.core.infrastructure.mysql.Connection

object Main {
  def main(args: Array[String]): Unit = {
    val environment = new Environment()
    println(environment.environment.manexLogin.id)
    println(environment.environment.manexLogin.password)
    println(environment.environment.mysql.kabu.user)
    println(environment.environment.mysql.kabu.password)
    println(environment.environment.mysql.kabu.host)
    println(environment.environment.mysql.kabu.port)
    new Connection()
  }
}
