package jozepoko.stock_trader.core.domain.repository.dao

import jozepoko.stock_trader.core.domain.entity.{StockPrice, FiveMinutelyStockPrice}
import jozepoko.stock_trader.core.domain.repository.dao.exception.InvalidMarketException
import jozepoko.stock_trader.core.domain.service.enum.{Market, MarketEnum}
import org.joda.time.DateTime
import scala.concurrent.Future
import scalikejdbc._
import scalikejdbc.async._

class FiveMinutelyStockPriceDao extends MysqlDao {
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

  def create(fiveMinutelyStockPrice: StockPrice)(implicit session: SharedAsyncDBSession = session, cxt: EC = ECGlobal): Future[FiveMinutelyStockPrice] = {
    for {
      result <- sql"""
        INSERT INTO five_minutely_stock_price VALUES(
          ${fiveMinutelyStockPrice.datetime},
          ${fiveMinutelyStockPrice.code},
          ${fiveMinutelyStockPrice.market.id},
          ${fiveMinutelyStockPrice.name},
          ${fiveMinutelyStockPrice.openingPrice},
          ${fiveMinutelyStockPrice.highPrice},
          ${fiveMinutelyStockPrice.lowPrice},
          ${fiveMinutelyStockPrice.closingPrice}
        )
      """.update().future
    } yield fiveMinutelyStockPrice.asInstanceOf[FiveMinutelyStockPrice]
  }

  def find(datetime: DateTime, code: Int, market: Market)(implicit session: SharedAsyncDBSession = session, cxt: EC = ECGlobal): Future[Option[FiveMinutelyStockPrice]] = {
    sql"""
      SELECT *
      FROM five_minutely_stock_price
      WHERE
        datetime = $datetime
        AND code = $code
        AND market = ${market.id}
    """.map(mapper).single().future
  }

  def delete(datetime: DateTime, code: Int, market: Market)(implicit session: SharedAsyncDBSession = session, cxt: EC = ECGlobal): Future[(DateTime, Int, Market)] = {
    for {
      result <- sql"""
        DELETE FROM five_minutely_stock_price
        WHERE
          datetime = $datetime
          AND code = $code
          AND market = ${market.id}
      """.update().future
    } yield (datetime, code, market)
  }
}
