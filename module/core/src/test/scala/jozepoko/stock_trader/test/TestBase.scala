package jozepoko.stock_trader.test

import org.specs2.mutable.Specification
import org.specs2.specification.{Step, Fragments, AfterExample, BeforeExample}

trait TestBase extends Specification with BeforeExample with AfterExample {
  override def before = {}
  override def after = {}
  override def map(fragments: => Fragments) = Step(setup) ^ fragments ^ Step(tearDown)
  def setup = {}
  def tearDown = {}
}
