package jozepoko.stock_trader.stock_price_complementer.domain.repository

import java.io.File
import jozepoko.stock_trader.core.domain.service.trade.enum.{MarketEnum, Market}
import jozepoko.stock_trader.core.domain.service.util.file.FileUtil
import jozepoko.stock_trader.core.infrastructure.file.SeparatedValuesReader
import jozepoko.stock_trader.stock_price_complementer.domain.entity.DailyMizStockPrice
import jozepoko.stock_trader.stock_price_complementer.domain.service.enum.{PriceUpDownEnum, PriceUpDown}
import scala.collection.mutable.ListBuffer
import scala.util.control.NonFatal

class MizFileReader(
  fileUtil: FileUtil = new FileUtil
) extends SeparatedValuesReader {
  def read[T](file: File): List[DailyMizStockPrice] = {
    val originalFileName = file.getAbsolutePath
    val newFileName = originalFileName.substring(0, originalFileName.length - 3) + "csv"
    val newFile = fileUtil.unzip(file, new File(newFileName)) match {
      case s :: Nil => s
      case _ => throw new Exception("Miz企画のファイルが不正だよ")
    }
    val list = ListBuffer[DailyMizStockPrice]()
    super.read(
      newFile.getAbsolutePath, ",", "", "SJIS"
    ) { columns =>
      try {
        list += DailyMizStockPrice(
          columns("コード").toInt,
          market(columns("市場")),
          columns("名称"),
          columns("始値").toDouble,
          columns("高値").toDouble,
          columns("安値").toDouble,
          columns("終値").toDouble,
          columns("出来高").toLong,
          columns("単元株数").toInt,
          columns("単元出来高").toInt,
          columns("PER").toDouble,
          columns("PBR").toDouble,
          columns("5日平均").toDouble,
          columns("25日平均").toDouble,
          columns("75日平均").toDouble,
          updown(columns("5日平均上下")),
          updown(columns("25日平均上下")),
          updown(columns("75日平均上下")),
          columns("出来高増減率").toDouble,
          columns("5日前からの上下率").toDouble,
          columns("前日終値").toDouble
        )
      } catch {
        case NonFatal(e) => println(e) //握りつぶす。TODO どうにかする
      }
    }
    fileUtil.delete(newFile)
    fileUtil.delete(file)
    list.toList
  }

  private def market(m: String): Market = {
    m match {
      case "東証1部" => MarketEnum.TousyouFirst
      case "東証2部" => MarketEnum.TousyouSecond
      case "ジャスダック" => MarketEnum.Jasdaq
      case "東証マザーズ" => MarketEnum.Mothers
      case _ => MarketEnum.Other
    }
  }

  private def updown(u: String): PriceUpDown = {
    PriceUpDownEnum.find(u) match {
      case Some(v) => v
      case None => PriceUpDownEnum.Other
    }
  }
}
