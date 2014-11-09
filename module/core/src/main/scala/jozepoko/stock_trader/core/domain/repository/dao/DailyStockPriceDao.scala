package jozepoko.stock_trader.core.domain.repository.dao

import jozepoko.stock_trader.core.domain.entity.DailyStockPrice
import jozepoko.stock_trader.core.domain.repository.dao.exception.InvalidMarketException
import jozepoko.stock_trader.core.domain.service.trade.enum.{MarketEnum, Market}
import org.joda.time.DateTime
import scala.concurrent.Future
import scalikejdbc._, async._

class DailyStockPriceDao extends MysqlDao {
  private val mapper = (rs: WrappedResultSet) => {
    val market = MarketEnum.find(rs.int("market")).getOrElse(
      throw new InvalidMarketException(s"市場のIDが不正です。 ID: ${rs.int("market")}")
    )
    DailyStockPrice(
      rs.jodaDateTime("datetime"),
      rs.int("code"),
      market,
      rs.string("name"),
      rs.int("opening_price"),
      rs.int("high_price"),
      rs.int("low_price"),
      rs.int("closing_price"),
      rs.double("per"),
      rs.double("pbr"),
      rs.double("five_days_average"),
      rs.double("twenty_five_days_average"),
      rs.double("seventy_five_days_average")
    )
  }

  def create(dailyStockPrice: DailyStockPrice)(implicit session: SharedAsyncDBSession = session, cxt: EC = ECGlobal): Future[DailyStockPrice] = {
    for {
      result <- sql"""
        INSERT INTO daily_stock_price VALUES(
          ${dailyStockPrice.datetime},
          ${dailyStockPrice.code},
          ${dailyStockPrice.market.id},
          ${dailyStockPrice.name},
          ${dailyStockPrice.openingPrice},
          ${dailyStockPrice.highPrice},
          ${dailyStockPrice.lowPrice},
          ${dailyStockPrice.closingPrice},
          ${dailyStockPrice.per},
          ${dailyStockPrice.pbr},
          ${dailyStockPrice.fiveDaysAverage},
          ${dailyStockPrice.twentyFiveDaysAverage},
          ${dailyStockPrice.seventyFiveDaysAverage}
        )
      """.update().future
    } yield dailyStockPrice
  }

  def find(datetime: DateTime, code: Int, market: Market)(implicit session: SharedAsyncDBSession = session, cxt: EC = ECGlobal): Future[Option[DailyStockPrice]] = {
    sql"""
      SELECT *
      FROM daily_stock_price
      WHERE
        datetime = $datetime
        AND code = $code
        AND market = ${market.id}
    """.map(mapper).single().future
  }

  def delete(datetime: DateTime, code: Int, market: Market)(implicit session: SharedAsyncDBSession = session, cxt: EC = ECGlobal): Future[(DateTime, Int, Market)] = {
    for {
      result <- sql"""
        DELETE FROM daily_stock_price
        WHERE
          datetime = $datetime
          AND code = $code
          AND market = ${market.id}
      """.update().future
    } yield (datetime, code, market)
  }
}
