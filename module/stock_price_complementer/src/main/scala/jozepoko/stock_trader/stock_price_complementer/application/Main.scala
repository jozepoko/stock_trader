package jozepoko.stock_trader.stock_price_complementer.application

import jozepoko.stock_trader.stock_price_complementer.domain.service.StockPriceComplementer

object Main {
  def main(args: Array[String]): Unit = {
    val result = new StockPriceComplementer().run(args)
//    val connection = Connection.connection
//    val dao = new MinutelyStockPriceDao
//    val datetime = new DateTime(2014, 10, 11, 1, 2, 3, 0)
//    val code = 1234
//    val market = MarketEnum.TousyouFirst
//    val minutelyStockPrice = MinutelyStockPrice(
//      datetime,
//      code,
//      market,
//      "hoge",
//      1,
//      2,
//      3,
//      4
//    )
//    val result = connection.localTx { implicit tx =>
//      for {
//        price <- dao.create(minutelyStockPrice)
//      } yield {
//        println("bbbbbbbbbbbbbbbbb")
//        price
//      }
//    }
//
//
//    val delete = result.map { r =>
//      dao.delete(datetime, code, market)
//    }
//    delete onSuccess {
//      case e => println("cccccccccccccccccccc")
//    }
//
//    println("aaaaaaaaaaaaaa")
//    Await.result(result, 5.second)
//    try{
//      new StockPriceComplementer().run(args)
//    } finally {
//      sys.exit()
//    }
  }
}
