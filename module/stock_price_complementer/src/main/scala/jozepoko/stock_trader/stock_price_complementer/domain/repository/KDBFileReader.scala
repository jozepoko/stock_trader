package jozepoko.stock_trader.stock_price_complementer.domain.repository

import java.io.File
import jozepoko.stock_trader.core.domain.service.util.file.FileUtil
import jozepoko.stock_trader.core.infrastructure.file.SeparatedValuesReader
import jozepoko.stock_trader.stock_price_complementer.domain.entity.{MinutelyKDBStockPrice, DailyKDBStockPrice}
import jozepoko.stock_trader.stock_price_complementer.domain.repository.setting.KDBFileSettings._
import jozepoko.stock_trader.stock_price_complementer.domain.service.KDBMarketParser
import scala.collection.immutable.ListMap
import scala.collection.mutable.ListBuffer
import scala.util.control.NonFatal

/**
 * 株価データダウンロードサイトからダウンロードしたファイルのリーダー。
 */
class KDBFileReader(
  fileUtil: FileUtil = new FileUtil,
  kdbMarketParser: KDBMarketParser = new KDBMarketParser
) extends SeparatedValuesReader {
  /**
   * ファイルを読み、日足の株価のリストを取得する。
   *
   * @param file ダウンロードしたファイル
   * @return 日足の株価のリスト
   */
  def readDailyFile(file: File): List[DailyKDBStockPrice] = {
    val list = ListBuffer[DailyKDBStockPrice]()
    super.read(
      file.getAbsolutePath,
      Separator,
      Enclosure,
      Encoding,
      HeaderStartLine
    ) { columns: ListMap[String, String] =>
      try {
        list += DailyKDBStockPrice(
          columns(CodeColumnName).take(CodeLength).toInt,  //コードカラムには不要な文字が入っており、先頭から指定の長さの文字列がコードとなる
          kdbMarketParser.parse(columns(MarketColumnName)),
          columns(NameColumnName),
          columns(BusinessTypeColumnName),
          columns(OpeningPriceColumnName).toDouble,
          columns(HighPriceColumnName).toDouble,
          columns(LowPriceColumnName).toDouble,
          columns(ClosingPriceColumnNanme).toDouble,
          columns(TurnoverColumnName).toLong,
          columns(SalesValueColumnName).toLong
        )
      } catch {
        case NonFatal(e) => //握りつぶす。TODO どうにかする
      }
    }
    list.toList
  }

  /**
   * ファイルを読み、分足、5分足の株価のリストを取得する。
   *
   * @param file ダウンロードしたファイル
   * @return 分足の株価のリスト
   */
  def readMinutelyFile(file: File): List[MinutelyKDBStockPrice] = {
    val list = ListBuffer[MinutelyKDBStockPrice]()
    super.read(
      file.getAbsolutePath,
      Separator,
      Enclosure,
      Encoding,
      HeaderStartLine
    ) { columns: ListMap[String, String] =>
      try {
        list += MinutelyKDBStockPrice(
          columns(DayColumnName),
          columns(TimeColumnName),
          columns(OpeningPriceColumnName).toDouble,
          columns(HighPriceColumnName).toDouble,
          columns(LowPriceColumnName).toDouble,
          columns(ClosingPriceColumnNanme).toDouble,
          columns(TurnoverColumnName).toLong,
          columns(SalesValueColumnName).toLong
        )
      } catch {
        case NonFatal(e) => //握りつぶす。TODO どうにかする
      }
    }
    list.toList
  }
}
