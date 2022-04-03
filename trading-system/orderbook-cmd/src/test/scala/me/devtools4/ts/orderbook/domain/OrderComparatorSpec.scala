package me.devtools4.ts.orderbook.domain

import me.devtools4.ts.dto.{Ask, Bid, Order, SimpleOrder, Ticker}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class OrderComparatorSpec extends AnyFunSuite
  with BeforeAndAfter
  with Matchers {

  private var sut: OrderComparator = _

  before {
    sut = OrderComparator()
  }

  case class Case(name: String, o1: Order, o2: Order, success: Boolean, result: Int)

  private val cases = Set(
    Case("1",
      SimpleOrder("1", Ticker("AAPL"), Bid, 99, 1000, 10),
      SimpleOrder("1", Ticker("AAPL"), Bid, 99, 1000, 10),
      success = true, 0),
    Case("2",
      SimpleOrder("1", Ticker("AAPL"), Bid, 99, 1000, 10),
      SimpleOrder("1", Ticker("GOOG"), Bid, 99, 1000, 10),
      success = false, 0),
    Case("3",
      SimpleOrder("1", Ticker("AAPL"), Bid, 99, 1000, 10),
      SimpleOrder("1", Ticker("AAPL"), Ask, 99, 1000, 10),
      success = false, 0),
  )

  for (Case(name, o1, o2, success, result) <- cases) {
    test(s"$name $o1 $o2 $success $result") {
      if (success) {
        assertResult(result)(sut.compare(o1, o2))
      } else {
        intercept[IllegalArgumentException] {
          sut.compare(o1, o2)
        }
      }
    }
  }
}