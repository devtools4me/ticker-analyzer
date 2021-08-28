package me.devtools4.ticker.df

import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

import scala.io.Source
import scala.util.Success

import me.devtools4.ticker.ops._

@RunWith(classOf[JUnitRunner])
class OhlcvTest extends AnyFunSuite {
  val csv = Source.fromResource("QCOM.csv").mkString

  test("png") {
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
      FileName("test.png").toOS { os =>
        os.write(bytes)
        true
      }
    }
  }

  test("sma") {
    val x = for {
      ohlcv <- Ohlcv.of(csv)
      bytes <- ohlcv.smaPng("Adj Close", 500, 500)
    } yield {
      bytes
    }
    assertResult(true)(x.isSuccess)
    val bytes = x.get
    assertResult(43500)(bytes.length)

    assertResult(Success(true)) {
      FileName("sma.png").toOS { os =>
        os.write(bytes)
        true
      }
    }
  }
}