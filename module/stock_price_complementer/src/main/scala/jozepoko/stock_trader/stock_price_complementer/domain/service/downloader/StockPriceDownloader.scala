package jozepoko.stock_trader.stock_price_complementer.domain.service.downloader

import jozepoko.stock_trader.core.domain.entity.{FiveMinutelyStockPrice, MinutelyStockPrice, DailyStockPrice}
import jozepoko.stock_trader.core.domain.service.util.file.FileUtil
import jozepoko.stock_trader.stock_price_complementer.domain.entity.{MinutelyKDBStockPrice, DailyKDBStockPrice, DailyMizStockPrice}
import jozepoko.stock_trader.stock_price_complementer.domain.repository.{KDBFileReader, MizFileReader}
import jozepoko.stock_trader.stock_price_complementer.domain.service.KDBMarketParser
import jozepoko.stock_trader.stock_price_complementer.domain.value.KDBStockUrl
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * 株価をダウンロードする処理を持つ。
 */
class StockPriceDownloader(
  kdbFileReader: KDBFileReader = new KDBFileReader,
  mizFileReader: MizFileReader = new MizFileReader,
  kdbMarketParser: KDBMarketParser = new KDBMarketParser,
  fileUtil: FileUtil = new FileUtil,
  kdbStockPricecDownloader: KDBStockPriceDownloader = new KDBStockPriceDownloader,
  mizStockPriceDownloader: MizStockPriceDownloader = new MizStockPriceDownloader
) {
  /**
   * 日足の株価をダウンロードする。
   * @param day 日
   * @return 株価のリスト
   */
  def downloadDailyStockPrice(day: DateTime): List[DailyStockPrice] = {
    val kdbFile = kdbStockPricecDownloader.downloadDailyStockPriceFromKDB(day)
    val mizFile = mizStockPriceDownloader.downloadDailyStockPriceFromMiz(day)
    val kdbs = kdbFileReader.readDailyFile(kdbFile)
    fileUtil.delete(kdbFile)
    val mizs = mizFileReader.read(mizFile)
    fileUtil.delete(mizFile)
    merge(day, kdbs, mizs)
  }

  /**
   * 5分足の株価をダウンロードする。
   * @param day 日
   * @return 株価のリスト
   */
  def downloadFiveMinutelyStockPrice(day: DateTime): List[List[FiveMinutelyStockPrice]] = {
    def parseMinutelyKDBStockPrice(kdbStockUrl: KDBStockUrl, minutelyKDBStockPrice: MinutelyKDBStockPrice): FiveMinutelyStockPrice = {
      val dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(s"${minutelyKDBStockPrice.day} ${minutelyKDBStockPrice.time}")
      FiveMinutelyStockPrice(
        dateTime,
        kdbStockUrl.code,
        kdbMarketParser.parse(kdbStockUrl.market),
        kdbStockUrl.name,
        minutelyKDBStockPrice.openingPrice,
        minutelyKDBStockPrice.highPrice,
        minutelyKDBStockPrice.lowPrice,
        minutelyKDBStockPrice.closingPrice
      )
    }

    for {
      stock <- kdbStockPricecDownloader.downloadStockListFromKDB
    } yield {
      Thread.sleep(1000 * 60)  // 60秒
      val file = kdbStockPricecDownloader.downloadFiveMinutelyStockPriceFromKDB(stock.originalCode, day)
      val result = kdbFileReader.readMinutelyFile(file).map(parseMinutelyKDBStockPrice(stock, _))
      fileUtil.delete(file)
      result
    }
  }

  /**
   * 分足の株価をダウンロードする。
   * @param day 日
   * @return 株価のリスト
   */
  def downloadMinutelyStockPrice(day: DateTime): List[List[MinutelyStockPrice]] = {
    def parseMinutelyKDBStockPrice(kdbStockUrl: KDBStockUrl, minutelyKDBStockPrice: MinutelyKDBStockPrice): MinutelyStockPrice = {
      val dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(s"${minutelyKDBStockPrice.day} ${minutelyKDBStockPrice.time}")
      MinutelyStockPrice(
        dateTime,
        kdbStockUrl.code,
        kdbMarketParser.parse(kdbStockUrl.market),
        kdbStockUrl.name,
        minutelyKDBStockPrice.openingPrice,
        minutelyKDBStockPrice.highPrice,
        minutelyKDBStockPrice.lowPrice,
        minutelyKDBStockPrice.closingPrice
      )
    }

    for {
      stock <- kdbStockPricecDownloader.downloadStockListFromKDB
    } yield{
      Thread.sleep(1000 * 60)  // 60秒
      val file = kdbStockPricecDownloader.downloadMinutelyStockPriceFromKDB(stock.originalCode, day)
      val result = kdbFileReader.readMinutelyFile(file).map(parseMinutelyKDBStockPrice(stock, _))
      fileUtil.delete(file)
      result
    }
  }

  /**
   * 株価データダウンロードサイトの株価とMiz企画の株価をマージする。
   * @param day 日
   * @param kdbs 株価データダウンロードサイトの株価のリスト
   * @param mizs Miz企画の株価のリスト
   * @return 株価のリスト
   */
  private def merge(day: DateTime, kdbs: List[DailyKDBStockPrice], mizs: List[DailyMizStockPrice]): List[DailyStockPrice] = {
    for {
      kdb <- kdbs
      miz <- mizs
      if kdb.code == miz.code && kdb.market == miz.market
    } yield DailyStockPrice(
      day,
      kdb.code,
      kdb.market,
      kdb.name,
      kdb.openingPrice,
      kdb.highPrice,
      kdb.lowPrice,
      kdb.closingPrice,
      miz.per,
      miz.pbr,
      miz.fiveDaysPriceAverage,
      miz.twentyFiveDaysPriceAverage,
      miz.seventyFiveDaysPriceAverage
    )
  }
}
