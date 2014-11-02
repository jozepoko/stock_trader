package jozepoko.stock_trader.core.domain.entity

case class Environment(
  manexLogin: ManexLogin,
  mysql: Mysql
)

case class ManexLogin(
  id: String,
  password: String
)

case class Mysql(
  user: String,
  password: String,
  host: String,
  port: Int
)
