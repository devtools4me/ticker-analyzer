package me.devtools4.ticker

import me.devtools4.ticker.api.{Command, History, OneMonth, OneYear, Quote, Sma, Start, UnknownCommand}
import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ApiTest extends AnyFunSuite {
  test("Commands") {
    assertResult(UnknownCommand("[] => List()"))(Command(""))
    assertResult(Start)(Command("/start"))
    assertResult(Quote("AAPL"))(Command("/quote/AAPL"))
    assertResult(UnknownCommand("[/history] => List(, history)"))(Command("/history"))
    assertResult(History("AAPL", OneMonth))(Command("/history/AAPL"))
    assertResult(History("AAPL", OneYear))(Command("/history/1y/AAPL"))
    assertResult(UnknownCommand("[/sma] => List(, sma)"))(Command("/sma"))
    assertResult(Sma("AAPL", OneMonth))(Command("/sma/AAPL"))
    assertResult(Sma("AAPL", OneYear))(Command("/sma/1y/AAPL"))
  }
}