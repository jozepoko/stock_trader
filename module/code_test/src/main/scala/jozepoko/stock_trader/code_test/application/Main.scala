package jozepoko.stock_trader.code_test.application

import jozepoko.stock_trader.core.domain.service.util.mail.MailSender
import jozepoko.stock_trader.core.domain.service.util.shell.ShellCommandExecutor

object Main {
  def main(args: Array[String]): Unit = {
    new MailSender().subject("コードからの送信テスト").message(
      """aaa
        |
        |bbb
      """.stripMargin
    ).sendMail()
  }
}
