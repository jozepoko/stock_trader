package jozepoko.stock_trader.core.infrastructure.mysql

import com.typesafe.config.ConfigFactory
import scalikejdbc._, async._

object Connection {
  private val config = ConfigFactory.load()
  private val scheme = config.getString("db.default.scheme")
  private val user = config.getString("db.default.user")
  private val password = config.getString("db.default.password")
  private val host = config.getString("db.default.host")
  private val port = config.getInt("db.default.port")

  private val url = s"jdbc:mysql://$host:$port/$scheme?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=exception&tinyInt1isBit=false"

  AsyncConnectionPool.add(
    'stock,
    url,
    user,
    password
  )
}
