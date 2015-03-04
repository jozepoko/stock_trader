package jozepoko.stock_trader.stock_price_complementer.domain.service

import jozepoko.stock_trader.core.domain.service.util.datetime._
import jozepoko.stock_trader.stock_price_complementer.domain.repository.dao.ComplementCompletedDao
import jozepoko.stock_trader.stock_price_complementer.domain.service.setting.MixInStockPriceComplementerSettings
import org.joda.time.DateTime

/**
 * ダウンロード対象の日のリストを作る処理を持つ。
 */
class DownloadDayListGenerator(
  complmenetCompletedDao: ComplementCompletedDao = new ComplementCompletedDao
) extends MixInStockPriceComplementerSettings {
  /**
   * ダウンロード対象の日のリストを作る。
   * ダウンロードがすでに終わった日から30日分がダウンロード対象の日となる。
   * @return 日のリスト
   */
  def generate: List[DateTime] = {
    val day = complmenetCompletedDao.find match {
      case Some(v) => v.day
      case None => stockPriceComplementerSettings.StartDateTime
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
