package me.devtools4.ticker.df

import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

import scala.io.Source
import scala.util.Success

@RunWith(classOf[JUnitRunner])
class OhlcvTest extends AnyFunSuite {

  test("png") {
    val csv = Source.fromResource("QCOM.csv").mkString
    val x = for {
      ohlcv <- Ohlcv.of(csv)
      bytes <- ohlcv.png("Adj Close", 500, 500)
    } yield {
      bytes
    }
    assertResult(true)(x.isSuccess)
    val bytes = x.get
    assertResult(37622)(bytes.length)

    assertResult(Success(true)) {
      fos4any("test.png") { os =>
        os.write(bytes)
        true
      }
    }
  }
}