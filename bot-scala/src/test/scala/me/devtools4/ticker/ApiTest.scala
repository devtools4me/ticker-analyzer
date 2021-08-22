package me.devtools4.ticker

import me.devtools4.ticker.api.{TickerCmd, HistoryCmd, OneMonth, OneYear, QuoteCmd, SmaCmd, StartCmd, UnknownCmd}
import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ApiTest extends AnyFunSuite {
  test("Commands") {
    assertResult(UnknownCmd("[] => List()"))(TickerCmd(""))
    assertResult(StartCmd)(TickerCmd("/start"))
    assertResult(QuoteCmd("AAPL"))(TickerCmd("/quote/AAPL"))
    assertResult(UnknownCmd("[/history] => List(, history)"))(TickerCmd("/history"))
    assertResult(HistoryCmd("AAPL", OneMonth))(TickerCmd("/history/AAPL"))
    assertResult(HistoryCmd("AAPL", OneYear))(TickerCmd("/history/1y/AAPL"))
    assertResult(UnknownCmd("[/sma] => List(, sma)"))(TickerCmd("/sma"))
    assertResult(SmaCmd("AAPL", OneMonth))(TickerCmd("/sma/AAPL"))
    assertResult(SmaCmd("AAPL", OneYear))(TickerCmd("/sma/1y/AAPL"))
  }
}