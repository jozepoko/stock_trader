package jozepoko.stock_trader.core.domain.service.util.mail.setting

import com.typesafe.config.ConfigFactory
import java.io.File

/**
 * メールの設定。
 */
object MailSettings {
  private val config =  ConfigFactory.parseFile(new File("src/main/resources/application.conf"))

  /** デフォルトの送信元 */
  val DefaultFrom = config.getString("mail.from")

  /** デフォルトのホスト */
  val DefaultHost = config.getString("mail.host")

  /** デフォルトのポート */
  val DefaultPort = config.getInt("mail.port")

  /** デフォルトの文字コード */
  val DefaultEncoding = "UTF-8"
}
