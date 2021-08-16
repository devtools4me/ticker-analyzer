package me.devtools4.ticker.df

import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

import java.time.LocalDate

@RunWith(classOf[JUnitRunner])
class EtfsTest extends AnyFunSuite {
  private val sut = Etfs.of("all.csv").get

  test("etfs") {
    assertResult(Some(Etf(
      "SPY",
      LocalDate.of(1993, 1, 29),
      "SPDR S&P 500 ETF Trust",
      "Equity: U.S. - Large Cap",
      "State Street Global Advisors",
      "0.09%",
      "$365.50B"))
    )(sut.find("SPY"))
  }
}