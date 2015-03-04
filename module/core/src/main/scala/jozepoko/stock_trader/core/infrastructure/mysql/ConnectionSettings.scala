package jozepoko.stock_trader.core.infrastructure.mysql

import com.typesafe.config.ConfigFactory
import java.io.File

object ConnectionSetting {
  private[this] val config =  ConfigFactory.parseFile(new File("src/main/resources/application.conf"))
  private[this] val scheme = if (System.getProperty("isInTest") == "true") config.getString("db.default.test_scheme")
                               else config.getString("db.default.scheme")
  case object StockConnectionSetting extends ConnectionSetting(
    "stock",
    scheme,
    config.getString("db.default.user"),
    config.getString("db.default.password"),
    config.getString("db.default.host"),
    config.getInt("db.default.port")
  )
}

sealed abstract class ConnectionSetting(
  val id: String,
  val scheme: String,
  val user: String,
  val password: String,
  val host: String,
  val port: Int
) {
  val driver = "com.mysql.jdbc.Driver"
  val url = s"jdbc:mysql://$host:$port/$scheme?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=exception&tinyInt1isBit=false"
}
