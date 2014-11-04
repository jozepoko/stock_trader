package jozepoko.stock_trader.core.infrastructure.mysql

import jozepoko.stock_trader.core.domain.entity.Environment
import scalikejdbc._, async._

object Connection {
  private val SchemeName = "kabu"

  private val mysql = new Environment().mysql

  private val url = s"jdbc:mysql://${mysql.host}:${mysql.port}/$SchemeName?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=exception&tinyInt1isBit=false"

  AsyncConnectionPool.add(
    'stock,
    url,
    mysql.user,
    mysql.password
  )

  def session = NamedAutoSession('stock)
}
