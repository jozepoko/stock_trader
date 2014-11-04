package jozepoko.stock_trader.core.domain.repository.dao

import jozepoko.stock_trader.core.domain.entity.FiveMinutelyStockPrice
import jozepoko.stock_trader.core.domain.repository.dao.exception.InvalidMarketException
import jozepoko.stock_trader.core.domain.service.enum.{Market, MarketEnum}
import org.joda.time.DateTime
import scalikejdbc._

class MinutelyStockPriceDao extends MysqlDao {
  private val mapper = (rs: WrappedResultSet) => {
    val market = MarketEnum.find(rs.int("market")).getOrElse(
      throw new InvalidMarketException(s"市場のIDが不正です。 ID: ${rs.int("market")}")
    )
    FiveMinutelyStockPrice(
      rs.jodaDateTime("datetime"),
      rs.int("code"),
      market,
      rs.string("name"),
      rs.int("opening_price"),
      rs.int("high_price"),
      rs.int("low_price"),
      rs.int("closing_price")
    )
  }

  def create(MinutelyStockPrice: FiveMinutelyStockPrice)(implicit session: DBSession = session): Unit = {
    sql"""
      INSERT INTO minutely_stock_price VALUES(
        ${MinutelyStockPrice.datetime},
        ${MinutelyStockPrice.code},
        ${MinutelyStockPrice.market.id},
        ${MinutelyStockPrice.name},
        ${MinutelyStockPrice.openingPrice},
        ${MinutelyStockPrice.highPrice},
        ${MinutelyStockPrice.lowPrice},
        ${MinutelyStockPrice.closingPrice}
      )
    """.update().apply()
  }

  def find(datetime: DateTime, code: Int, market: Market)(implicit session: DBSession = session): Option[FiveMinutelyStockPrice] = {
    sql"""
      SELECT *
      FROM minutely_stock_price
      WHERE
        datetime = $datetime
        AND code = $code
        AND market = ${market.id}
    """.map(mapper).single().apply()
  }
}
