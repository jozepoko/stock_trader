package jozepoko.stock_trader.core.domain.repository.dao

import scalikejdbc.NamedAutoSession

trait MysqlDao extends Dao {
  protected def session = NamedAutoSession('stock)
}
