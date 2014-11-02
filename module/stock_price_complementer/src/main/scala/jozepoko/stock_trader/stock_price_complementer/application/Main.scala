package jozepoko.stock_trader.stock_price_complementer.application

import jozepoko.stock_trader.core.infrastructure.environment.Environment

object Main {
  def main(args: Array[String]): Unit = {
    val environment = new Environment()
    println(environment.environment.manexLogin.id)
    println(environment.environment.manexLogin.password)
    println(environment.environment.mysql.user)
    println(environment.environment.mysql.password)
    println(environment.environment.mysql.host)
    println(environment.environment.mysql.port)
  }
}
