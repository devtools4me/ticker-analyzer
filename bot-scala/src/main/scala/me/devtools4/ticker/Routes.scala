package me.devtools4.ticker

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import com.yahoo.finanance.query1.Quote
import me.devtools4.ticker.service.Tickers
import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._

final class Routes[F[_]: Sync](tickers: Tickers[F]) extends Http4sDsl[F] {
  implicit def encodeProduct[A[_]: Applicative]: EntityEncoder[A, Quote] = jsonEncoderOf

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "quote" / sym =>
      for {
        rows <- tickers.quote(sym)
        resp <- rows match {
          case Right(v) => Ok(v)
          case Left(_) => NotFound()
        }
      } yield resp
  }
}