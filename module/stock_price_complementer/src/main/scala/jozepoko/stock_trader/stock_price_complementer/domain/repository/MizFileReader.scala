package jozepoko.stock_trader.stock_price_complementer.domain.repository

import java.io.File
import jozepoko.stock_trader.core.domain.service.trade.enum.{MarketEnum, Market}
import jozepoko.stock_trader.core.domain.service.util.file.FileUtil
import jozepoko.stock_trader.core.infrastructure.file.SeparatedValuesReader
import jozepoko.stock_trader.stock_price_complementer.domain.entity.DailyMizStockPrice
import jozepoko.stock_trader.stock_price_complementer.domain.repository.setting.MizFileSettings._
import jozepoko.stock_trader.stock_price_complementer.domain.service.enum.{PriceUpDownEnum, PriceUpDown}
import scala.collection.mutable.ListBuffer
import scala.util.control.NonFatal

/**
 * Miz企画からダウンロードしたファイルのリーダー
 */
class MizFileReader(
  fileUtil: FileUtil = new FileUtil
) extends SeparatedValuesReader {
  /**
   * ファイルを読み、株価のリストを取得する
   * @param file Miz企画のファイル
   * @return 株価のリスト
   */
  def read(file: File): List[DailyMizStockPrice] = {
    val originalFileName = file.getAbsolutePath
    val newFileName = originalFileName.substring(0, originalFileName.length - 3) + "csv"
    val newFile = fileUtil.unzip(file, new File(newFileName)) match {
      case s :: Nil => s
      case _ => throw new Exception("Miz企画のファイルが不正だよ")
    }
    val list = ListBuffer[DailyMizStockPrice]()
    super.read(
      newFile.getAbsolutePath,
      Separator,
      Enclosure,
      Encoding
    ) { columns =>
      try {
        list += DailyMizStockPrice(
          columns(CodeColumnName).toInt,
          market(columns(MarketColumnName)),
          columns(NameColumnName),
          columns(OpeningPriceColumnName).toDouble,
          columns(HighPriceColumnName).toDouble,
          columns(LowPriceColumnName).toDouble,
          columns(ClosingPriceColumnNanme).toDouble,
          columns(TurnoverColumnName).toLong,
          columns(ShareUnitNumberColumnName).toInt,
          columns(ShareUnitSalesValueColumnName).toInt,
          columns(PERColumnName).toDouble,
          columns(PBRColumnName).toDouble,
          columns(FiveDaysPriceAverage).toDouble,
          columns(TwentyFiveDaysPriceAverage).toDouble,
          columns(SeventyFiveDaysPriceAverage).toDouble,
          updown(columns(fiveDaysPriceAverageUpDown)),
          updown(columns(TwentyFiveDaysPriceAverageUpDown)),
          updown(columns(SeventyFiveDaysPriceAverageUpDown)),
          columns(SalesValueChangeRate).toDouble,
          columns(SalesValueFromeFiveDaysBeforeChangeRate).toDouble,
          columns(BeforeDayClosingPrice).toDouble
        )
      } catch {
        case NonFatal(e) => println(e) //握りつぶす。TODO どうにかする
      }
    }
    fileUtil.delete(newFile)
    fileUtil.delete(file)
    list.toList
  }

  /**
   * Miz企画のファイルの「市場」をパースし、Market型に変換する
   * @param m String型の市場
   * @return Market型の市場
   */
  private def market(m: String): Market = {
    m match {
      case "東証1部" => MarketEnum.TousyouFirst
      case "東証2部" => MarketEnum.TousyouSecond
      case "ジャスダック" => MarketEnum.Jasdaq
      case "東証マザーズ" => MarketEnum.Mothers
      case _ => MarketEnum.Other
    }
  }

  /**
   * Miz企画のファイルの「平均上下」をパースし、PriceUpDown型に変換する
   * @param u String型の平均上下
   * @return PriceUpDown型の平均上下
   */
  private def updown(u: String): PriceUpDown = {
    PriceUpDownEnum.find(u) match {
      case Some(v) => v
      case None => PriceUpDownEnum.Other
    }
  }
}
