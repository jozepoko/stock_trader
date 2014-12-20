package jozepoko.stock_trader.stock_price_complementer.domain.service.downloader

import jozepoko.stock_trader.core.domain.entity.{FiveMinutelyStockPrice, MinutelyStockPrice, DailyStockPrice}
import jozepoko.stock_trader.core.domain.service.util.file.FileUtil
import jozepoko.stock_trader.core.domain.service.util.html.HtmlParserBuilder
import jozepoko.stock_trader.core.infrastructure.http._
import jozepoko.stock_trader.stock_price_complementer.domain.entity.{MinutelyKDBStockPrice, DailyKDBStockPrice, DailyMizStockPrice}
import jozepoko.stock_trader.stock_price_complementer.domain.repository.{KDBFileReader, MizFileReader}
import jozepoko.stock_trader.stock_price_complementer.domain.service.KDBMarketParser
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class StockPriceDownloader(
  protected val request: Request = new Request,
  kdbFileReader: KDBFileReader = new KDBFileReader,
  mizFileReader: MizFileReader = new MizFileReader,
  kdbMarketParser: KDBMarketParser = new KDBMarketParser,
  fileUtil: FileUtil = new FileUtil,
  protected val htmlParserBuilder: HtmlParserBuilder = new HtmlParserBuilder
) extends KDBStockPriceDownloader
with MizStockPriceDownloader {
  def downloadDailyStockPrice(day: DateTime): List[DailyStockPrice] = {
    val kdbFile = downloadDailyStockPriceFromKDB(day)
    val mizFile = downloadDailyStockPriceFromMiz(day)
    val kdbs = kdbFileReader.readDailyFile(kdbFile)
    fileUtil.delete(kdbFile)
    val mizs = mizFileReader.read(mizFile)
    fileUtil.delete(mizFile)
    merge(day, kdbs, mizs)
  }

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

    println(downloadStockList)
    for {
      stock <- downloadStockList
    } yield {
      Thread.sleep(1000 * 60)
      val file = downloadFiveMinutelyStockPriceFromKDB(stock.originalCode, day)
      val result = kdbFileReader.readMinutelyFile(file).map(parseMinutelyKDBStockPrice(stock, _))
      fileUtil.delete(file)
      result
    }
  }

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
      stock <- downloadStockList
    } yield{
      val file = downloadMinutelyStockPriceFromKDB(stock.originalCode, day)
      val result = kdbFileReader.readMinutelyFile(file).map(parseMinutelyKDBStockPrice(stock, _))
      fileUtil.delete(file)
      result
    }
  }

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
