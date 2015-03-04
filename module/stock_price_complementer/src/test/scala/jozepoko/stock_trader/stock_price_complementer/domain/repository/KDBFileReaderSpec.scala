package jozepoko.stock_trader.stock_price_complementer.domain.repository

import java.io.File
import jozepoko.stock_trader.stock_price_complementer.domain.entity.MinutelyKDBStockPrice
import jozepoko.stock_trader.test.TestBase

/**
 * KDBFileReaderのテスト
 */
class KDBFileReaderSpec extends TestBase with MixInKDBFileReader {
  "KDBFileReader" should "分足の株価のcsvを読み、エンティティに変換できる" in {
    val actual =  kdbFileReader.readMinutelyFile(new File("module/stock_price_complementer/src/test/scala/jozepoko/stock_trader/stock_price_complementer/domain/repository/fixture/KDBMinutelyStockPriceFile.csv"))
    assert(actual === List(
      MinutelyKDBStockPrice("2015-03-04", "14:57", 11, 12, 13, 14, 15, 16),
      MinutelyKDBStockPrice("2015-03-04", "14:56", 21, 22, 23, 24, 25, 26),
      MinutelyKDBStockPrice("2015-03-04", "14:54", 41, 42, 43, 44, 45, 46)
    ))
  }
}
