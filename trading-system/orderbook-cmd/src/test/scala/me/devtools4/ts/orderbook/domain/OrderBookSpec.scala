package me.devtools4.ts.orderbook.domain

import me.devtools4.ts.dto.Order.toOrders
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.junit.JUnitRunner

import scala.util.Properties

@RunWith(classOf[JUnitRunner])
class OrderBookSpec extends AnyFunSuite
  with BeforeAndAfter
  with Matchers {

  private var sut: OrderBook = _

  before {
    sut = OrderBook(OrderContainer(), OrderContainer())
  }

  case class Case(name: String, list: List[String], success: Boolean, str: String)

  private val cases = Set(
    Case("1st", List("10000,AAPL,B,99,1000", "10001,AAPL,B,98,1200", "10002,AAPL,B,99,500"), success = true,
      "     1,000     99 |                  " + Properties.lineSeparator +
      "       500     99 |                  " + Properties.lineSeparator +
      "     1,200     98 |                  "
  ))

  for (Case(name, list, success, str) <- cases) {
    test(s"$name $list $success $str") {
      sut.add(toOrders(list))
      assertResult(str)(sut.str)
    }
  }
}