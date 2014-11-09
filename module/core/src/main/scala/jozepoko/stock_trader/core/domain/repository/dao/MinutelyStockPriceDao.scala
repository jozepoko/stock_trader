package jozepoko.stock_trader.core.domain.repository.dao

import jozepoko.stock_trader.core.domain.entity.{StockPrice, MinutelyStockPrice}
import jozepoko.stock_trader.core.domain.repository.dao.exception.InvalidMarketException
import jozepoko.stock_trader.core.domain.service.trade.enum.{Market, MarketEnum}
import org.joda.time.DateTime
import scala.concurrent.Future
import scalikejdbc._
import scalikejdbc.async._

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

  def create(minutelyStockPrice: StockPrice)(implicit session: SharedAsyncDBSession = session, cxt: EC = ECGlobal): Future[MinutelyStockPrice] = {
    for {
      result <- sql"""
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
      """.update().future
    } yield minutelyStockPrice.asInstanceOf[MinutelyStockPrice]
  }

  def find(datetime: DateTime, code: Int, market: Market)(implicit session: SharedAsyncDBSession = session, cxt: EC = ECGlobal): Future[Option[MinutelyStockPrice]] = {
    sql"""
      SELECT *
      FROM minutely_stock_price
      WHERE
        datetime = $datetime
        AND code = $code
        AND market = ${market.id}
    """.map(mapper).single().future
  }

  def delete(datetime: DateTime, code: Int, market: Market)(implicit session: SharedAsyncDBSession = session, cxt: EC = ECGlobal): Future[(DateTime, Int, Market)] = {
    for {
      result <- sql"""
        DELETE FROM minutely_stock_price
        WHERE
          datetime = $datetime
          AND code = $code
          AND market = ${market.id}
      """.update().future
    } yield (datetime, code, market)
  }
}
