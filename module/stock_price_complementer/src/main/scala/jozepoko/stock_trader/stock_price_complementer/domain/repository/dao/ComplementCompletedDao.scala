package jozepoko.stock_trader.stock_price_complementer.domain.repository.dao

import jozepoko.stock_trader.core.domain.repository.dao.MysqlDao
import jozepoko.stock_trader.stock_price_complementer.domain.value.ComplementCompleted
import org.joda.time.DateTime
import scalikejdbc._

class ComplementCompletedDao extends MysqlDao {
  private val mapper = (rs: WrappedResultSet) => ComplementCompleted(rs.jodaDateTime("day"))

  def find(implicit session: DBSession = session): Option[ComplementCompleted] = {
    sql"""
      SELECT *
      FROM complement_completed
      LIMIT 1
    """.map(mapper).single().apply()
  }

  def truncate(implicit session: DBSession = session): Int = {
    sql"""
      TRUNCATE TABLE complement_completed
    """.update().apply()
  }

  def replace(day: DateTime)(implicit session: DBSession = session): DateTime = {
    sql"""
      REPLACE INTO complement_completed VALUES ($day)
    """.update().apply()
    day
  }
}

