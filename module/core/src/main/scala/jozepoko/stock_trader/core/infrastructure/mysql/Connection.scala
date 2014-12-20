package jozepoko.stock_trader.core.infrastructure.mysql

import com.typesafe.config.ConfigFactory
import java.io.File
import scalikejdbc._

object Connection {
  scalikejdbc.GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG,
    warningEnabled = true,
    warningThresholdMillis = 600000,
    warningLogLevel = 'WARN
  )

  private val config =  ConfigFactory.parseFile(new File("src/main/resources/application.conf"))
  private val scheme = config.getString("db.default.scheme")
  private val user = config.getString("db.default.user")
  private val password = config.getString("db.default.password")
  private val host = config.getString("db.default.host")
  private val port = config.getInt("db.default.port")

  //TODO この書き方ができない。調べろ
  private val url = s"jdbc:mysql://$host:$port/$scheme?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=exception&tinyInt1isBit=false"

  ConnectionPool.add(
    'stock,
    url,
    user,
    password
  )

  val connection = NamedDB('stock)
}
