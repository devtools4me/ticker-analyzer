package me.devtools4.ticker

import me.devtools4.ticker.api.{TickerCmd, History, OneMonth, OneYear, Quote, Sma, Start, UnknownCmd}
import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ApiTest extends AnyFunSuite {
  test("Commands") {
    assertResult(UnknownCmd("[] => List()"))(TickerCmd(""))
    assertResult(Start)(TickerCmd("/start"))
    assertResult(Quote("AAPL"))(TickerCmd("/quote/AAPL"))
    assertResult(UnknownCmd("[/history] => List(, history)"))(TickerCmd("/history"))
    assertResult(History("AAPL", OneMonth))(TickerCmd("/history/AAPL"))
    assertResult(History("AAPL", OneYear))(TickerCmd("/history/1y/AAPL"))
    assertResult(UnknownCmd("[/sma] => List(, sma)"))(TickerCmd("/sma"))
    assertResult(Sma("AAPL", OneMonth))(TickerCmd("/sma/AAPL"))
    assertResult(Sma("AAPL", OneYear))(TickerCmd("/sma/1y/AAPL"))
  }
}