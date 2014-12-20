package jozepoko.stock_trader.stock_price_complementer.domain.service

import jozepoko.stock_trader.core.domain.repository.dao.{MinutelyStockPriceDao, FiveMinutelyStockPriceDao, DailyStockPriceDao}
import jozepoko.stock_trader.core.domain.service.util.datetime._
import jozepoko.stock_trader.core.infrastructure.http.Request
import jozepoko.stock_trader.core.infrastructure.mysql.Connection
import jozepoko.stock_trader.stock_price_complementer.domain.repository.KDBFileReader
import jozepoko.stock_trader.stock_price_complementer.domain.repository.dao.ComplementCompletedDao
import jozepoko.stock_trader.stock_price_complementer.domain.service.downloader.StockPriceDownloader
import jozepoko.stock_trader.stock_price_complementer.domain.service.enum.ModeEnum.{BeforeDay, Past}
import org.joda.time.DateTime
import scalikejdbc.DBConnection

class StockPriceComplementer(
  stockPriceDownloader: StockPriceDownloader = new StockPriceDownloader,
  dailyStockPriceDao: DailyStockPriceDao = new DailyStockPriceDao,
  fiveMinutelyStockPriceDao: FiveMinutelyStockPriceDao = new FiveMinutelyStockPriceDao,
  minutelyStockPriceDao: MinutelyStockPriceDao = new MinutelyStockPriceDao,
  connection: DBConnection = Connection.connection,
  downloadDayListGenerator: DownloadDayListGenerator = new DownloadDayListGenerator,
  complementCompletedDao: ComplementCompletedDao = new ComplementCompletedDao,
  kdbFileReader: KDBFileReader = new KDBFileReader,
  request: Request = new Request
) {
  def run(args: Array[String]): Unit = {
    val argument = ArgumentParser.parse(args)
    argument.mode match {
      case Past => complementPast()
      case BeforeDay => complementBeforeDay()
    }
  }

  private def complementPast() = {
    val dayList = downloadDayListGenerator.generate
    dayList foreach { day =>
      val stockPrices = stockPriceDownloader.downloadDailyStockPrice(day)
      stockPrices.grouped(1000).map { groupedStockPrices =>
        connection localTx { implicit session =>
          dailyStockPriceDao.replaces(groupedStockPrices)
        }
        Thread.sleep(1000 * 5)
      }
    }
    complementCompletedDao.truncate
    complementCompletedDao.replace(dayList.last)
  }

  private def complementBeforeDay() = {
    val yesterDay = DateTime.now.minusDays(1).withTime(0, 0, 0, 0)
    if (yesterDay.isTradableDateTime) {
      stockPriceDownloader.downloadFiveMinutelyStockPrice(yesterDay) foreach { fiveMinutelyEntityList =>
        connection localTx { implicit session =>
          fiveMinutelyStockPriceDao.replaces(fiveMinutelyEntityList)
        }
      }
      stockPriceDownloader.downloadMinutelyStockPrice(yesterDay) foreach { minutelyEntityList =>
        connection localTx { implicit session =>
          minutelyStockPriceDao.replaces(minutelyEntityList)
        }
      }
    }
  }
}
