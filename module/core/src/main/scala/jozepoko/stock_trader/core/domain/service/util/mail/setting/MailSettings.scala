package jozepoko.stock_trader.core.domain.service.util.mail.setting

import com.typesafe.config.ConfigFactory
import java.io.File

/**
 * メールの設定。
 */
object MailSettings {
  private val config =  ConfigFactory.parseFile(new File("src/main/resources/application.conf"))

  /** デフォルトの送信者名 */
  val DefaultName = config.getString("mail.name")

  val DefaultTo = config.getString("mail.to")
}
