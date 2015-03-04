package jozepoko.stock_trader.core.infrastructure.mysql

import scalikejdbc.{LoggingSQLAndTimeSettings, NamedDB}

trait UsesStockConnectionPool { def stockConnectionPool: StockConnectionPool }

trait MixInStockConnectionPool extends UsesStockConnectionPool {
  val stockConnectionPool: StockConnectionPool = new StockConnectionPool with MixInConnectionPool
}

abstract class StockConnectionPool extends UsesConnectionPool {
  def borrow: NamedDB = connectionPool.borrow(ConnectionSetting.StockConnectionSetting)
}

trait UsesConnectionPool { def connectionPool: ConnectionPool }

trait MixInConnectionPool extends UsesConnectionPool {
  val connectionPool: ConnectionPool = new ConnectionPool
}

class ConnectionPool {
  def borrow(connectionSetting: ConnectionSetting): NamedDB = {
    Connector.connect(connectionSetting)
    NamedDB(connectionSetting.id)
  }
}

object Connector {
  var isConnected = false

  def connect(connectionSetting: ConnectionSetting): Unit = {
    if (!isConnected) {
      scalikejdbc.GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
        enabled = true,
        singleLineMode = true,
        logLevel = 'DEBUG,
        warningEnabled = true,
        warningThresholdMillis = 5000,
        warningLogLevel = 'WARN
      )
      Class.forName(connectionSetting.driver)
      scalikejdbc.ConnectionPool.add(
        connectionSetting.id,
        connectionSetting.url,
        connectionSetting.user,
        connectionSetting.password
      )
      isConnected = true
    }
  }
}
