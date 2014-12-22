package jozepoko.stock_trader.core.domain.service.util.mail

import jozepoko.stock_trader.core.domain.service.util.mail.exception.{MailSendFailedException, DestinationNotDefinedException}
import jozepoko.stock_trader.core.domain.service.util.mail.setting.MailSettings
import org.apache.commons.mail.SimpleEmail
import scala.util.control.NonFatal

/**
 * メール送信を扱うutil。
 */
class MailSender(
  var from: String = MailSettings.DefaultFrom,
  var to: Seq[String] = Nil,
  var cc: Seq[String] = Nil,
  var bcc: Seq[String] = Nil,
  var subject: String = "",
  var message: String = "",
  var encoding: String = MailSettings.DefaultEncoding,
  var host: String = MailSettings.DefaultHost,
  var port: Int = MailSettings.DefaultPort
) {
  /**
   * メールを送信する。
   */
  def sendMail(): Unit = {
    if (to.isEmpty && cc.isEmpty && bcc.isEmpty) throw new DestinationNotDefinedException("メールの送信先が設定されていませ")
    try {
      val mail = new SimpleEmail
      mail.setFrom(from)
      to.foreach(mail.addTo)
      cc.foreach(mail.addCc)
      bcc.foreach(mail.addBcc)
      mail.setSubject(subject)
      mail.setMsg(message)
      mail.setHostName(host)
      mail.setSmtpPort(port)
      mail.setCharset(encoding)
    } catch {
      case NonFatal(e) => throw new MailSendFailedException(s"メールの送信に失敗しました。エラーメッセージ : ${e.getMessage}")
    }
  }

  /**
   * 送信元を変更する。
   * @param f 送信先
   */
  def from(f: String): MailSender = {
    from = f
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

  /**
   * 文字コードを変更する。
   * @param e 文字コード
   */
  def encoding(e: String): MailSender = {
    encoding = e
    this
  }
  
  /**
   * ホストを変更する。
   * @param h ホスト
   */
  def host(h: String): MailSender = {
    host = h
    this
  }

  /**
   * ポートを変更する。
   * @param p ポート
   */
  def port(p: Int): MailSender = {
    port = p
    this
  }
}
