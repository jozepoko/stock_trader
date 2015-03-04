package jozepoko.stock_trader.test

import jozepoko.stock_trader.core.infrastructure.mysql.MixInStockConnectionPool
import org.scalatest.FlatSpec
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.{Step, Fragments, AfterExample, BeforeExample}

trait TestBase extends FlatSpec with MixInStockConnectionPool {
  def testDBConnection = stockConnectionPool.borrow
}

trait Specs2TestBase extends Specification with BeforeExample with AfterExample with Mockito with MixInStockConnectionPool {
  override def before = {}
  override def after = {}
  override def map(fragments: => Fragments) = Step(setup) ^ fragments ^ Step(tearDown)
  def setup = {}
  def tearDown = {}
  def testDBConnection = stockConnectionPool.borrow
}
