package jozepoko.stock_trader.stock_price_complementer.application

import jozepoko.stock_trader.stock_price_complementer.domain.service.StockPriceComplementer

object Main {
  def main(args: Array[String]): Unit = {
    try {
      new StockPriceComplementer().run(args)
    } finally {
      sys.exit()
    }
  }
}
