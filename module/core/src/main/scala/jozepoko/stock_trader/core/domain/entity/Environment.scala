package jozepoko.stock_trader.core.domain.entity

import com.typesafe.config.ConfigFactory

case class Environment(
  manexLogin: ManexLogin = new ManexLogin,
  mysql: Mysql = new Mysql
)

case class ManexLogin(
  id: String = ConfigFactory.load().getString("stockCompany.manex.id"),
  password: String = ConfigFactory.load().getString("stockCompany.manex.password")
)

case class Mysql(
  user: String = ConfigFactory.load().getString("db.default.user"),
  password: String = ConfigFactory.load().getString("db.default.password"),
  host: String = ConfigFactory.load().getString("db.default.host"),
  port: Int = ConfigFactory.load().getInt("db.default.port")
)
