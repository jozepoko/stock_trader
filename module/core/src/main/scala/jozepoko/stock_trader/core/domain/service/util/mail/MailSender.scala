package jozepoko.stock_trader.core.domain.service.util.mail

import jozepoko.stock_trader.core.domain.service.util.mail.exception.{MailSendFailedException, DestinationNotDefinedException}
import jozepoko.stock_trader.core.domain.service.util.mail.setting.MailSettings
import jozepoko.stock_trader.core.domain.service.util.shell.ShellCommandExecutor
import scala.util.control.NonFatal

/**
 * メール送信を扱うutil。
 */
class MailSender(
  var name: String = MailSettings.DefaultName,
  var to: Seq[String] = List(MailSettings.DefaultTo),
  var cc: Seq[String] = Nil,
  var bcc: Seq[String] = Nil,
  var subject: String = "",
  var message: String = "",
  val shellCommandExecutor: ShellCommandExecutor = new ShellCommandExecutor
) {
  /**
   * メールを送信する。
   */
  def sendMail(): Unit = {
    if (to.isEmpty && cc.isEmpty && bcc.isEmpty) throw new DestinationNotDefinedException("メールの送信先が設定されていませ")
    try {
      val echoCommand = s"""echo -e "$message" """
      val emailCommand = new StringBuilder
      emailCommand.append(s"""email -b -s "$subject" -n "$name" """)
      if (cc.nonEmpty) emailCommand.append(s"-cc ${cc.mkString(",")}")
      if (bcc.nonEmpty) emailCommand.append(s"-bcc ${bcc.mkString(",")}")
      if (to.nonEmpty) emailCommand.append(to.mkString("\"", "\", \"", "\""))
      shellCommandExecutor.sendMail(echoCommand, emailCommand.result()) match {
        case Left(e) => throw new Exception(e)
        case _ =>  //何もしない
      }
    } catch {
      case NonFatal(e) => throw new MailSendFailedException(s"メールの送信に失敗しました。エラーメッセージ : ${e.getMessage}")
    }
  }

  /**
   * 送信元を変更する。
   * @param n 送信先
   */
  def name(n: String): MailSender = {
    name = n
    this
  }

  /**
   * 送信先を変更する。
   * @param t 送信先
   */
  def to(t: Seq[String]): MailSender = {
    to = t
    this
  }

  /**
   * 送信先を変更する。
   * @param t 送信先
   */
  def to(t: String): MailSender = {
    to = Seq(t)
    this
  }

  /**
   * ccを変更する。
   * @param c cc
   */
  def cc(c: Seq[String]): MailSender = {
    cc = c
    this
  }

  /**
   * ccを変更する。
   * @param c cc
   */
  def cc(c: String): MailSender = {
    cc = Seq(c)
    this
  }

  /**
   * bccを変更する。
   * @param b bcc
   */
  def bcc(b: Seq[String]): MailSender = {
    bcc = b
    this
  }

  /**
   * bccを変更する。
   * @param b bcc
   */
  def bcc(b: String): MailSender = {
    bcc = Seq(b)
    this
  }

  /**
   * 件名を変更する。
   * @param b 件名
   */
  def subject(b: String): MailSender = {
    subject = b
    this
  }

  /**
   * 本文を変更する。
   * @param m 本文
   */
  def message(m: String): MailSender = {
    message = m
    this
  }
}
