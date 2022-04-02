package me.devtools4.ts.dto

import me.devtools4.ts.dto.Side.toSide
import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SideSpec extends AnyFunSuite with Matchers {

  case class Case(name: String, s: String, success: Boolean, result: String, str: String)

  private val cases = Set(
    Case("Empty string", "", success = false, "", ""),
    Case("Parse error", "A", success = false, "", ""),
    Case("Bid", "B", success = true, "Bid", "B"),
    Case("Ask", "S", success = true, "Ask", "S")
  )

  for (Case(name, s, success, result, str) <- cases) {
    test(s"$name $s $success $result $str") {
      val opt = toSide(s)
      assertResult(success)(opt.isDefined)
      opt.foreach { side =>
        assertResult(result)(side.toString)
        assertResult(str)(side.value)
      }
    }
  }
}