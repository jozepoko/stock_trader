package jozepoko.stock_trader.core.infrastructure.mysql

import jozepoko.stock_trader.core.infrastructure.environment.Environment
import scalikejdbc._

object Connection {
  Class.forName("com.mysql.jdbc.Driver")

  private val SchemeName = "kabu"

  private val mysql = new Environment().environment.mysql

  private val url = s"jdbc:mysql://${mysql.host}:${mysql.port}/$SchemeName?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=exception&tinyInt1isBit=false"

  private val connectionPoolSettings = new ConnectionPoolSettings(
    initialSize = 1,
    maxSize = 5
  )
  
  private val ConnectionName = "stock"

  ConnectionPool.add(
    ConnectionName,
    url,
    mysql.user,
    mysql.password,
    connectionPoolSettings
  )

  def session = NamedAutoSession(ConnectionName)
}
