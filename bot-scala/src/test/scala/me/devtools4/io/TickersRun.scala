package me.devtools4.io

import cats.effect._
import cats.implicits._
import com.yahoo.finanance.query1.sttp.SttpQuery1ApiPClient
import me.devtools4.io.debug._
import me.devtools4.ticker.api.OneYear
import me.devtools4.ticker.ops.FileName
import me.devtools4.ticker.repository.CsvEtfRepository
import me.devtools4.ticker.service.IoTickers

object TickersRun extends IOApp {
  private val client = SttpQuery1ApiPClient("https://query1.finance.yahoo.com")
  private val ETFs = CsvEtfRepository("all.csv").get
  private val tickers = IoTickers(client, ETFs)

  def run(args: List[String]): IO[ExitCode] = {

    List(program("VIOO"), program("SPY"), program("GLDM"))
      .parSequence
      .debug
      .map(println(_))
      .as(ExitCode.Success)
  }

  def program(sym: String): IO[Unit] = {
    val a = tickers.quote(sym)
      .debug
    val b = tickers.history(sym, OneYear)
      .debug
    val c = tickers.sma(sym, OneYear)
      .debug
    (a, b, c).parMapN { (x, y, z) =>
      println(x)
      y.foreach { arr =>
        FileName(s"$sym-hist.png").toOS { os =>
          os.write(arr)
          true
        }
      }
      z.foreach { arr =>
        FileName(s"$sym-sma.png").toOS { os =>
          os.write(arr)
          true
        }
      }
    }.debug
  }
}