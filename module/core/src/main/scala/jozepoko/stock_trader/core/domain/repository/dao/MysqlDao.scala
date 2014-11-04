package jozepoko.stock_trader.core.domain.repository.dao

import scalikejdbc.async.{ShortenedNames, NamedAsyncDB}

trait MysqlDao extends Dao with ShortenedNames {
  protected def session = NamedAsyncDB('stock).sharedSession
}
