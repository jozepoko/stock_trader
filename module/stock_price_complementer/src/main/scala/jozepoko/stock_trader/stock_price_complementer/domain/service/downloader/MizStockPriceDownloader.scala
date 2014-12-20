package jozepoko.stock_trader.stock_price_complementer.domain.service.downloader

import java.io.File
import jozepoko.stock_trader.core.infrastructure.http._
import jozepoko.stock_trader.stock_price_complementer.domain.service.setting.StockPriceComplementerSettings
import org.joda.time.DateTime

trait MizStockPriceDownloader {
  protected val request: Request

  protected def downloadDailyStockPriceFromMiz(day: DateTime): File = {
    val file = new File(filePath(day))
    request.url(createDailyMizUrl(day)).download(file)
  }

  private def createDailyMizUrl(day: DateTime): String = {
    val year = day.toString("yyyy").substring(2)
    s"http://mizkikaku.web.fc2.com/data/kabu$year${day.toString("MMdd")}.zip"
  }

  private def filePath(day: DateTime): String = {
    s"${StockPriceComplementerSettings.MizDataDirectoryPath}/miz_${day.toString("yyyyMMddHHmmss")}.zip"
  }
}
