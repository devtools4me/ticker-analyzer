package me.devtools4.io

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import com.yahoo.finanance.query1.Quote._
import com.yahoo.finanance.query1.sttp.SttpQuery1ApiClient

object SttpIoRun extends App {

  import me.devtools4.ticker.df._

  implicit val runtime: IORuntime = cats.effect.unsafe.implicits.global

  val client = SttpQuery1ApiClient("https://query1.finance.yahoo.com")
  val sym = "SPY"

  val program = for {
    q <- IO {
      client.quote(sym).toOption
        .flatMap(_.quoteResponse.result.headOption)
    }
    etf <- IO {
      etfs("all.csv").toOption
        .flatMap(_.find(sym))
    }
    html <- IO {
      (q, etf) match {
        case (Some(qq), Some(ee)) =>
          qq.copyWith(ee.expenseRatio, ee.aum).html
        case _ => ???
      }
    }
  } yield {
    println(html)
  }

  program.unsafeRunSync()
}
