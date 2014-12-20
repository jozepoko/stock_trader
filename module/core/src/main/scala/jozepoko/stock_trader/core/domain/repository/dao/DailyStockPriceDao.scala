package jozepoko.stock_trader.core.domain.repository.dao

import jozepoko.stock_trader.core.domain.entity.DailyStockPrice
import jozepoko.stock_trader.core.domain.repository.dao.exception.InvalidMarketException
import jozepoko.stock_trader.core.domain.service.trade.enum.{MarketEnum, Market}
import org.joda.time.DateTime
import scalikejdbc._

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

  def create(dailyStockPrice: DailyStockPrice)(implicit session: DBSession = session): DailyStockPrice = {
    sql"""
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
        ${dailyStockPrice.fiveDaysPriceAverage},
        ${dailyStockPrice.twentyFiveDaysPriceAverage},
        ${dailyStockPrice.seventyFiveDaysPriceAverage}
      )
    """.update().apply()
    dailyStockPrice
  }

  def replaces(dailyStockPrices: List[DailyStockPrice])(implicit session: DBSession = session): List[DailyStockPrice] = {
    val sql =
      s"REPLACE INTO daily_stock_price VALUES ${dailyStockPrices.map { dailyStockPrice => s"('${dailyStockPrice.datetime.toString("yyyy-MM-dd HH:mm:ss")}', '${dailyStockPrice.code}', '${dailyStockPrice.market.id}', '${dailyStockPrice.name}', '${dailyStockPrice.openingPrice}', '${dailyStockPrice.highPrice}', '${dailyStockPrice.lowPrice}', '${dailyStockPrice.closingPrice}', '${dailyStockPrice.per}', '${dailyStockPrice.pbr}', '${dailyStockPrice.fiveDaysPriceAverage}', '${dailyStockPrice.twentyFiveDaysPriceAverage}', '${dailyStockPrice.seventyFiveDaysPriceAverage}')" }.mkString(",")}"
    SQL(sql).update().apply()
    dailyStockPrices
  }

  def find(datetime: DateTime, code: Int, market: Market)(implicit session: DBSession = session): Option[DailyStockPrice] = {
    sql"""
      SELECT *
      FROM daily_stock_price
      WHERE
        datetime = $datetime
        AND code = $code
        AND market = ${market.id}
    """.map(mapper).single().apply()
  }

  def delete(datetime: DateTime, code: Int, market: Market)(implicit session: DBSession = session): (DateTime, Int, Market) = {
    sql"""
      DELETE FROM daily_stock_price
      WHERE
        datetime = $datetime
        AND code = $code
        AND market = ${market.id}
    """.update().apply()
    (datetime, code, market)
  }
}
