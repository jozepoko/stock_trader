package jozepoko.stock_trader.test

import com.typesafe.config.ConfigFactory
import java.io.File
import org.scalatest.FlatSpec
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.{Step, Fragments, AfterExample, BeforeExample}
import scalikejdbc.{NamedDB, ConnectionPool, LoggingSQLAndTimeSettings}

trait TestBase extends FlatSpec

trait Specs2TestBase extends Specification with BeforeExample with AfterExample with Mockito {
  override def before = {}
  override def after = {}
  override def map(fragments: => Fragments) = Step(setup) ^ fragments ^ Step(tearDown)
  def setup = {}
  def tearDown = {}
  def testDBConnection = TestConnection.testDBConection
}

object TestConnection {
  scalikejdbc.GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG,
    warningEnabled = true,
    warningThresholdMillis = 600000,
    warningLogLevel = 'WARN
  )

  private val config =  ConfigFactory.parseFile(new File("src/main/resources/application.conf"))
  private val scheme = "kabu_test"
  private val user = config.getString("db.default.user")
  private val password = config.getString("db.default.password")
  private val host = config.getString("db.default.host")
  private val port = config.getInt("db.default.port")

  //TODO この書き方ができない。調べろ
  private val url = s"jdbc:mysql://$host:$port/$scheme?useUnicode=yes&characterEncoding=UTF-8&zeroDateTimeBehavior=exception&tinyInt1isBit=false"

  ConnectionPool.add(
    'stock_test,
    url,
    user,
    password
  )

  def testDBConection = NamedDB('stock_test)
}
