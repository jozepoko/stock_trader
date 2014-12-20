package jozepoko.stock_trader.stock_price_complementer.domain.service

import jozepoko.stock_trader.core.domain.service.util.datetime._
import jozepoko.stock_trader.stock_price_complementer.domain.repository.dao.ComplementCompletedDao
import jozepoko.stock_trader.stock_price_complementer.domain.service.setting.StockPriceComplementerSettings
import org.joda.time.DateTime

class DownloadDayListGenerator(
  complmenetCompletedDao: ComplementCompletedDao = new ComplementCompletedDao
) {
  def generate: List[DateTime] = {
    val day = complmenetCompletedDao.find match {
      case Some(v) => v.day
      case None => StockPriceComplementerSettings.StartDateTime
    }
    collectTradableDays(day).take(30).toList
  }

  private def collectTradableDays(day: DateTime): Stream[DateTime] = {
    if (day.isTradableDateTime) {
      day #:: collectTradableDays(day.plusDays(1))
    } else {
      collectTradableDays(day.plusDays(1))
    }
  }
}
