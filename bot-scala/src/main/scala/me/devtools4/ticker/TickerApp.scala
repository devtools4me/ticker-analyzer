package me.devtools4.ticker

import cats.implicits._
import cats.effect.unsafe.implicits.global
import cats.effect.{ExitCode, IO, IOApp}
import com.yahoo.finanance.query1.sttp.SttpQuery1ApiPClient
import me.devtools4.ticker.repository.CsvEtfRepository
import me.devtools4.ticker.routes.{TickerRoutes, WebhookRoutes}
import me.devtools4.ticker.service.IoTickers
import me.devtools4.ticker.telegram.UpdateHandler
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object TickerApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val program = for {
      repo <- IO.fromTry(CsvEtfRepository("all.csv"))
      client = SttpQuery1ApiPClient("https://query1.finance.yahoo.com")
      tickers = IoTickers(client, repo)
      tickerRoutes = new TickerRoutes(tickers)
      webhookRoutes = new WebhookRoutes(tickers)
      routes = tickerRoutes.routes <+> webhookRoutes.routes
      httpApp = Router("/" -> routes).orNotFound
      server = BlazeServerBuilder[IO](ExecutionContext.global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(httpApp)
      fiber = server.resource.use(_ => IO(StdIn.readLine())).as(ExitCode.Success)
    } yield fiber
    program.attempt.unsafeRunSync() match {
      case Left(e) =>
        IO {
          println("*** An error occured! ***")
          if (e ne null) {
            println(e.getMessage)
          }
          ExitCode.Error
        }
      case Right(r) => r
    }
  }
}