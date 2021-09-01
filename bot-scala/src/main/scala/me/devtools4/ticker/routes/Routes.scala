package me.devtools4.ticker.routes

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import com.yahoo.finanance.query1.Quote
import me.devtools4.ticker.api.{OneMonth, Period}
import me.devtools4.ticker.service.Tickers
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import org.typelevel.ci.CIString

final class Routes[F[_]: Sync](tickers: Tickers[F]) extends Http4sDsl[F] {
  implicit def encodeProduct[A[_]: Applicative]: EntityEncoder[A, Quote] = jsonEncoderOf

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "quote" / sym =>
      for {
        q <- tickers.quote(sym)
        resp <- q match {
          case Right(v) => Ok(v)
          case Left(err) => NotFound(err)
        }
      } yield resp

    case GET -> Root / "history" / sym =>
      for {
        hist <- tickers.history(sym, OneMonth)
        resp <- hist match {
          case Right(arr) => ok(sym, arr)
          case Left(err) => NotFound(err)
        }
      } yield resp

    case GET -> Root / "history" / period / sym =>
      for {
        hist <- tickers.history(sym, Period(period).getOrElse(OneMonth))
        resp <- hist match {
          case Right(arr) => ok(sym, arr)
          case Left(err) => NotFound(err)
        }
      } yield resp

    case GET -> Root / "sma" / sym =>
      for {
        hist <- tickers.sma(sym, OneMonth)
        resp <- hist match {
          case Right(arr) => ok(sym, arr)
          case Left(err) => NotFound(err)
        }
      } yield resp

    case GET -> Root / "sma" / period / sym =>
      for {
        hist <- tickers.sma(sym, Period(period).getOrElse(OneMonth))
        resp <- hist match {
          case Right(arr) => ok(sym, arr)
          case Left(err) => NotFound(err)
        }
      } yield resp
  }

  private def ok(sym: String, arr: Array[Byte]) = Ok(arr,
    Header.Raw(CIString("Content-Type"), "image/png"),
    Header.Raw(CIString("Content-Disposition"), s"attachment; filename=\"$sym.png\""))
}