package jozepoko.stock_trader.stock_price_complementer.domain.service

import jozepoko.stock_trader.core.domain.service.util.datetime._
import jozepoko.stock_trader.stock_price_complementer.domain.repository.dao.ComplementCompletedDao
import jozepoko.stock_trader.stock_price_complementer.domain.value.ComplementCompleted
import jozepoko.stock_trader.test.Specs2TestBase
import org.joda.time.DateTime

class DownloadDayListGeneratorSpec extends Specs2TestBase {
  "DownloadDayListGenerator" should {
    "ダウンロード対象の日のリストを作れる" in {
      val daoMock = mock[ComplementCompletedDao]
      daoMock.find returns Some(ComplementCompleted(new DateTime(2011, 11, 11, 0, 0, 0)))
      val generator = new DownloadDayListGenerator(daoMock)
      val actual = generator.generate
      actual.length mustEqual 30
      actual.filter(! _.isTradableDateTime) mustEqual Nil
    }
  }
}
