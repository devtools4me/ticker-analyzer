package me.devtools4.io

import cats.effect._
import cats.implicits._
import com.yahoo.finanance.query1.Quote._
import com.yahoo.finanance.query1.sttp.SttpQuery1ApiClient
import me.devtools4.io.debug._
import me.devtools4.ticker.repository.CsvEtfRepository

object SttpIoRun extends IOApp {
  private val client = SttpQuery1ApiClient("https://query1.finance.yahoo.com")
  private val ETFs = CsvEtfRepository("all.csv").get

  def f(i: IO[Unit]): IO[IO[Unit]] = IO(i)

  def run(args: List[String]): IO[ExitCode] = {

    List(program("VIOO"), program("SPY"), program("GLDM"))
      .parSequence
      .debug
      .map(println(_))
      .as(ExitCode.Success)
  }

  def program(sym: String): IO[Product] = {
    val a = IO.blocking {
      client.quote(sym).toOption
        .flatMap(_.quoteResponse.result.headOption)
    }.debug
    val b = ETFs
      .find(sym)
      .debug
    (a, b).parMapN { (x, y) =>
      (x, y) match {
        case (Some(q), h :: t) =>
          q.copyWith(h.expenseRatio, h.aum).html
        case (Some(q), _) =>
          q
        case _ => throw new RuntimeException()
      }
    }.debug
  }
}
