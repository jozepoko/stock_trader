package jozepoko.stock_trader.core.domain.repository.dao

import jozepoko.stock_trader.core.domain.entity.MinutelyStockPrice
import jozepoko.stock_trader.core.domain.repository.dao.exception.InvalidMarketException
import jozepoko.stock_trader.core.domain.service.trade.enum.{Market, MarketEnum}
import org.joda.time.DateTime
import scalikejdbc._

class MinutelyStockPriceDao extends MysqlDao {
  private val mapper = (rs: WrappedResultSet) => {
    val market = MarketEnum.find(rs.int("market")).getOrElse(
      throw new InvalidMarketException(s"市場のIDが不正です。 ID: ${rs.int("market")}")
    )
    MinutelyStockPrice(
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

  def create(minutelyStockPrice: MinutelyStockPrice)(implicit session: DBSession = session): MinutelyStockPrice = {
    sql"""
      INSERT INTO minutely_stock_price VALUES(
        ${minutelyStockPrice.datetime},
        ${minutelyStockPrice.code},
        ${minutelyStockPrice.market.id},
        ${minutelyStockPrice.name},
        ${minutelyStockPrice.openingPrice},
        ${minutelyStockPrice.highPrice},
        ${minutelyStockPrice.lowPrice},
        ${minutelyStockPrice.closingPrice}
      )
    """.update().apply()
    minutelyStockPrice
  }

  def replaces(minutelyStockPrices: List[MinutelyStockPrice])(implicit session: DBSession = session): List[MinutelyStockPrice] = {
    val sql = s"""REPLACE INTO minutely_stock_price VALUES
      |${minutelyStockPrices.map { minutelyStockPrice =>
      s"""('${minutelyStockPrice.datetime.toString("yyyy-MM-dd HH:mm:ss")}',
           |'${minutelyStockPrice.code}',
           |'${minutelyStockPrice.market.id}',
           |'${minutelyStockPrice.name}',
           |'${minutelyStockPrice.openingPrice}',
           |'${minutelyStockPrice.highPrice}',
           |'${minutelyStockPrice.lowPrice}',
           |'${minutelyStockPrice.closingPrice}'
           |)""".stripMargin
    }.mkString(",")}
      |
    """.stripMargin
    SQL(sql).update().apply()
    minutelyStockPrices
  }

  def find(datetime: DateTime, code: Int, market: Market)(implicit session: DBSession = session): Option[MinutelyStockPrice] = {
    sql"""
      SELECT *
      FROM minutely_stock_price
      WHERE
        datetime = $datetime
        AND code = $code
        AND market = ${market.id}
    """.map(mapper).single().apply()
  }

  def delete(datetime: DateTime, code: Int, market: Market)(implicit session: DBSession = session): (DateTime, Int, Market) = {
    sql"""
      DELETE FROM minutely_stock_price
      WHERE
        datetime = $datetime
        AND code = $code
        AND market = ${market.id}
    """.update().apply()
    (datetime, code, market)
  }
}
