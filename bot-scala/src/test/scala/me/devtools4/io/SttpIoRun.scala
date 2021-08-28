package me.devtools4.io

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import com.yahoo.finanance.query1.{Quote, QuoteResponseResponse}
import com.yahoo.finanance.query1.sttp.SttpQuery1ApiClient
import sttp.client3.quick.{backend, basicRequest}
import io.circe.parser._
import sttp.client3._

import Quote._

import scala.util.{Failure, Success}

object SttpIoRun extends App {

  import me.devtools4.ticker.df._
  import me.devtools4.ticker.ops._

  implicit val runtime: IORuntime = cats.effect.unsafe.implicits.global

  val basePath = "https://query1.finance.yahoo.com"
  val client = SttpQuery1ApiClient(basePath)
  val sym = "SPY"

  val opt = client.quote(sym)
    .toOption
    .map(_.quoteResponse.result)
    .flatMap(_.headOption)

  val t = etfs("all.csv")

  for {
    res <- client.quote(sym).toOption
    q <- res.quoteResponse.result.headOption
    all <- etfs("all.csv").toOption
    etf <- all.find(sym)
  } yield {
    println(q.copyWith(etf.expenseRatio, etf.aum).html)
  }

  val program = for {
    x <- IO {
      basicRequest.get(uri"$basePath/v7/finance/quote?symbols=$sym").send(backend).body
        .flatMap(x => decode[QuoteResponseResponse](x))
    }
    t <- IO {
      x.fold(l => Failure(new RuntimeException(l.toString)), r => Success(r))
    }
    _ <- IO {
      println(t)
    }
  } yield {
  }

  program.unsafeRunSync()
}
