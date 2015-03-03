package jozepoko.stock_trader.core.domain.repository.dao

import jozepoko.stock_trader.core.domain.entity.MinutelyStockPrice
import jozepoko.stock_trader.core.domain.service.trade.enum.{Market, MarketEnum}
import jozepoko.stock_trader.core.infrastructure.mysql.Connection
import jozepoko.stock_trader.test.TestBase
import org.joda.time.DateTime
import scalikejdbc._

class MinutelyStockPriceDaoSpec extends TestBase {
  val minutelyStockPriceDao = new MinutelyStockPriceDao

  "MinutelyStockPriceDao" should {
    "分足の株価を保存できる" in {
      val fixture = MinutelyStockPrice(
        new DateTime(2015, 1 ,1, 0, 0, 0, 0),
        123,
        MarketEnum.Jasdaq,
        "name",
        1,
        2,
        3,
        4
      )
      val actual = testDBConnection.localTx { implicit session =>
        minutelyStockPriceDao.create(fixture)
        minutelyStockPriceDao.find(
          new DateTime(2015, 1, 1, 0, 0, 0, 0),
          123,
          MarketEnum.Jasdaq
        )
      }
      actual mustEqual Some(fixture)
    }

    "分足の株価をリプレイスできる" in {
      def createMinutelyStockPrice(dateTime: DateTime, code: Int, market: Market, name: String) =
        MinutelyStockPrice(
          dateTime,
          code,
          market,
          name,
          1,
          2,
          3,
          4
        )
      val fixtures = List(
        createMinutelyStockPrice(new DateTime(2015, 1, 1, 0, 0, 0, 0), 1, MarketEnum.Jasdaq, "name1"),
        createMinutelyStockPrice(new DateTime(2015, 1, 2, 0, 0, 0, 0), 1, MarketEnum.Jasdaq, "name2"),
        createMinutelyStockPrice(new DateTime(2015, 1, 3, 0, 0, 0, 0), 1, MarketEnum.Jasdaq, "name3")
      )
      testDBConnection.localTx { implicit session =>
        fixtures.foreach(minutelyStockPriceDao.create)
        minutelyStockPriceDao.replaces(
          List(
            createMinutelyStockPrice(new DateTime(2015, 1, 1, 0, 0, 0, 0), 1, MarketEnum.Jasdaq, "name11"),
            createMinutelyStockPrice(new DateTime(2015, 1, 2, 0, 0, 0, 0), 1, MarketEnum.Jasdaq, "name22")
          )
        )
      }
      testDBConnection.localTx { implicit session =>
        minutelyStockPriceDao.find(new DateTime(2015, 1, 1, 0, 0, 0, 0), 1, MarketEnum.Jasdaq) mustEqual Some(
          createMinutelyStockPrice(new DateTime(2015, 1, 1, 0, 0, 0, 0), 1, MarketEnum.Jasdaq, "name11")
        )
        minutelyStockPriceDao.find(new DateTime(2015, 1, 2, 0, 0, 0, 0), 1, MarketEnum.Jasdaq) mustEqual Some(
          createMinutelyStockPrice(new DateTime(2015, 1, 2, 0, 0, 0, 0), 1, MarketEnum.Jasdaq, "name22")
        )
        minutelyStockPriceDao.find(new DateTime(2015, 1, 3, 0, 0, 0, 0), 1, MarketEnum.Jasdaq) mustEqual Some(
          createMinutelyStockPrice(new DateTime(2015, 1, 3, 0, 0, 0, 0), 1, MarketEnum.Jasdaq, "name3")
        )
      }
      true must beTrue
    }
  }

  override def setup = {
    testDBConnection.localTx { implicit session =>
      sql"truncate table minutely_stock_price".update().apply()
    }
  }


  override def tearDown = {
    testDBConnection.localTx { implicit session =>
      sql"truncate table minutely_stock_price".update().apply()
    }
  }
}
