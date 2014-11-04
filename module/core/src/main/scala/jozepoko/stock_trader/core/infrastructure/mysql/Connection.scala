package jozepoko.stock_trader.core.infrastructure.mysql

import jozepoko.stock_trader.core.infrastructure.environment.Environment
import scalikejdbc._

object Connection {
  Class.forName("com.mysql.jdbc.Driver")

  private val SchemeName = "kabu"

  private val kabu = new Environment().environment.mysql.kabu

  private val url = s"jdbc:mysql://${kabu.host}:${kabu.port}/$SchemeName?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=exception&tinyInt1isBit=false"

  private val connectionPoolSettings = new ConnectionPoolSettings(
    initialSize = 1,
    maxSize = 5
  )
  
  private val ConnectionName = "stock"

  ConnectionPool.add(
    ConnectionName,
    url,
    kabu.user,
    kabu.password,
    connectionPoolSettings
  )

  def session = NamedAutoSession(ConnectionName)
}
