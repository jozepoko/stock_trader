package jozepoko.stock_trader.core.domain.repository.dao

import jozepoko.stock_trader.core.infrastructure.mysql.Connection

trait MysqlDao extends Dao {
  protected def session = Connection.session
}
