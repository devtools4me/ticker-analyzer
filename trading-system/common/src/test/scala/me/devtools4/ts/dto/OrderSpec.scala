package me.devtools4.ts.dto

import com.typesafe.scalalogging.LazyLogging
import me.devtools4.ts.dto.Order.toOrder
import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class OrderSpec extends AnyFunSuite with Matchers with LazyLogging {

  case class Case(name: String, s: String, success: Boolean, result: String, str: String)

  private val cases = Set(
    Case("Empty string", "", success = false, "", ""),
    Case("Parse error", "10,000,B,1000000,100", success = false, "", ""),
    Case("Enum error", "10,1000,B,100", success = false, "", ""),
    Case("Price error", "10000,B,-100,100", success = false, "", ""),
    Case("Price error", "10000,B,0,100", success = false, "", ""),
    Case("Price error", "10000,B,1000000,100", success = false, "", ""),
    Case("Volume error", "10000,B,100,-100", success = false, "", ""),
    Case("Volume error", "10000,B,100,0", success = false, "", ""),
    Case("Volume error", "10000,B,100,1000000000", success = false, "", ""),
    Case("Order", "10000,AAPL,B,99,50000", success = true, "SimpleOrder(10000,Ticker(AAPL),Bid,99,50000,0)", "    50,000     99"),
    Case("Ice Order", "ice1,AAPL,B,100,100000,10000", success = false, "orderId=ice1, side=B, price=100, volume=100000, visibleQuantity=10000, lastTradedVolume=0, time=0", "    10,000    100")
  )

  for (Case(name, s, success, result, str) <- cases) {
    test(s"$name $s $success $result $str") {
      val opt = toOrder(s, 0)
      assertResult(success)(opt.isDefined)
      opt.foreach { o =>
        assertResult(result)(o.toString)
        assertResult(str)(o.str)
      }
    }
  }
}