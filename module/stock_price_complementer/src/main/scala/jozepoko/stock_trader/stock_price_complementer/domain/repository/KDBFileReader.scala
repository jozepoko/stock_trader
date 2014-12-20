package jozepoko.stock_trader.stock_price_complementer.domain.repository

import java.io.File
import jozepoko.stock_trader.core.domain.service.trade.enum.{Market, MarketEnum}
import jozepoko.stock_trader.core.domain.service.util.file.FileUtil
import jozepoko.stock_trader.core.infrastructure.file.SeparatedValuesReader
import jozepoko.stock_trader.stock_price_complementer.domain.entity.{MinutelyKDBStockPrice, DailyKDBStockPrice}
import scala.collection.immutable.ListMap
import scala.collection.mutable.ListBuffer
import scala.util.control.NonFatal

/**
 * 株価データダウンロードサイトからダウンロードしたファイルのリーダー。
 */
class KDBFileReader(
  fileUtil: FileUtil = new FileUtil
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
      ",",
      "",
      "SJIS",
      2
    ) { columns: ListMap[String, String] =>
      try {
        list += DailyKDBStockPrice(
          columns("コード").take(4).toInt,
          market(columns("市場")),
          columns("銘柄名"),
          columns("業種"),
          columns("始値").toDouble,
          columns("高値").toDouble,
          columns("安値").toDouble,
          columns("終値").toDouble,
          columns("出来高").toLong,
          columns("売買代金").toLong
        )
      } catch {
        case NonFatal(e) => println(e) //握りつぶす。TODO どうにかする
      }
    }
    fileUtil.delete(file)
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
      ",",
      "",
      "SJIS",
      2
    ) { columns: ListMap[String, String] =>
      try {
        list += MinutelyKDBStockPrice(
          columns("日付"),
          columns("時刻"),
          columns("始値").toDouble,
          columns("高値").toDouble,
          columns("安値").toDouble,
          columns("終値").toDouble,
          columns("出来高").toLong,
          columns("売買代金").toLong
        )
      } catch {
        case NonFatal(e) => println(e) //握りつぶす。TODO どうにかする
      }
    }
    //TODO 不要ならファイルを消すようにする
    //fileUtil.delete(file)
    list.toList
  }

  /**
   * 株価データダウンロードサイトのファイルの「市場」をパースし、Market型に変換する。
   * @param m String型の市場
   * @return Market型の市場
   */
  def market(m: String): Market = {
    m match {
      case "東証1部" => MarketEnum.TousyouFirst
      case "東証2部" => MarketEnum.TousyouSecond
      case s if s.startsWith("JQ") => MarketEnum.Jasdaq
      case "東証マザーズ" => MarketEnum.Mothers
      case _ => MarketEnum.Other
    }
  }
}
