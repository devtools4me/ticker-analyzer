package me.devtools4.ticker.routes

import cats.effect.Concurrent
import cats.implicits._
import me.devtools4.ticker.service.Tickers
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import com.bot4s.telegram.models.Update
import com.bot4s.telegram.marshalling._

final class WebhookRoutes[F[_] : Concurrent](tickers: Tickers[F]) extends Http4sDsl[F] {
  implicit def decodeProduct: EntityDecoder[F, Update] = jsonOf[F, Update]

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req@POST -> Root / "callback" / "webhook" =>
      for {
        u <- req.as[Update]
        res <- Ok()
      } yield {
        res
      }
  }
}