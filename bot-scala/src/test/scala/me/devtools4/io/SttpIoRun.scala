package me.devtools4.io

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import com.yahoo.finanance.query1.QuoteResponseResponse
import sttp.client3.quick.{backend, basicRequest}
import io.circe.parser._
import sttp.client3._

import scala.util.{Failure, Success}

object SttpIoRun extends App {
  implicit val runtime: IORuntime = cats.effect.unsafe.implicits.global

  val basePath = "https://query1.finance.yahoo.com"
  val symbol_id = "AAPL"

  val program = for {
    x <- IO {
      basicRequest.get(uri"$basePath/v7/finance/quote?symbols=$symbol_id").send(backend).body
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
