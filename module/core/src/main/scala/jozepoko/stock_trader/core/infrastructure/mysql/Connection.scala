package jozepoko.stock_trader.core.infrastructure.mysql

import jozepoko.stock_trader.core.domain.entity.Mysql
import jozepoko.stock_trader.core.infrastructure.environment.Environment
import scalikejdbc._

class Connection(
  mysqlEnvironment: Mysql = new Environment().environment.mysql
) {
  Class.forName("com.mysql.jdbc.Driver")

  val schemeName = "kabu"

  val kabu = mysqlEnvironment.kabu

  val url = s"jdbc:mysql://${kabu.host}:${kabu.port}/${schemeName}?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=exception&tinyInt1isBit=false"

  val connectionPoolSettings = new ConnectionPoolSettings(
    initialSize = 1,
    maxSize = 5
  )

  ConnectionPool.add(
    "stock",
    url,
    kabu.user,
    kabu.password,
    connectionPoolSettings
  )
}
