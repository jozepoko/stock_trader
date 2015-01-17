package jozepoko.stock_trader.stock_price_complementer.domain.service

import jozepoko.stock_trader.core.domain.service.util.mail.MailSender
import jozepoko.stock_trader.stock_price_complementer.domain.service.enum.ModeEnum.{BeforeDay, Past}

class StockPriceComplementer(
  mailSender: MailSender = new MailSender,
  pastComplementer: PastComplementer = new PastComplementer,
  beforeDayComplementer: BeforeDayComplementer = new BeforeDayComplementer
) {
  def run(args: Array[String]): Unit = {
    val argument = ArgumentParser.parse(args)
    val (subject, message) = argument.mode match {
      case Past => pastComplementer.complementPast()
      case BeforeDay => beforeDayComplementer.complementBeforeDay()
    }
    mailSender.name("stock_price_complementer").subject(subject).message(message).sendMail()
  }
}
