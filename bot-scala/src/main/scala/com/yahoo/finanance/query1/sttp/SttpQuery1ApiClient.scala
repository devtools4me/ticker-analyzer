package com.yahoo.finanance.query1.sttp

import cats.effect.IO
import com.yahoo.finanance.query1.{Query1Api, Query1ApiP, QuoteResponseResponse}
import io.circe.parser._
import sttp.client3._

class SttpQuery1ApiClient(basePath: String, backend: SttpBackend[Identity, Any]) extends Query1Api {
  override def quote(symbol_id: String): Either[String, QuoteResponseResponse] =
    basicRequest.get(uri"$basePath/v7/finance/quote?symbols=$symbol_id")
      .send(backend)
      .body
      .flatMap { json =>
        decode[QuoteResponseResponse](json) match {
          case Left(err) => Left(s"${err.toString}, json=$json")
          case Right(value) => Right(value)
        }
      }

  override def download(symbol_id: String, interval: String, from_date: Long, to_date: Long): Either[String, String] =
    basicRequest.get(uri"$basePath/v7/finance/download/$symbol_id?interval=$interval&period1=$from_date&period2=$to_date")
      .send(backend)
      .body
}

object SttpQuery1ApiClient {
  def apply(basePath: String): SttpQuery1ApiClient =
    SttpQuery1ApiClient(basePath, HttpURLConnectionBackend())

  def apply(basePath: String, backend: SttpBackend[Identity, Any]): SttpQuery1ApiClient =
    new SttpQuery1ApiClient(basePath, backend)
}

class SttpQuery1ApiPClient(basePath: String, backend: SttpBackend[Identity, Any]) extends Query1ApiP[IO] {
  override def quote(symbol_id: String): IO[QuoteResponseResponse] =
    IO.blocking {
      basicRequest.get(uri"$basePath/v7/finance/quote?symbols=$symbol_id")
        .send(backend)
        .body
        .flatMap { json =>
          decode[QuoteResponseResponse](json)
        }.fold(err => Left(new RuntimeException(err.toString)), x => Right(x))
    }.flatMap(IO.fromEither(_))

  override def download(symbol_id: String, interval: String, from_date: Long, to_date: Long): IO[String] =
    IO.blocking {
      basicRequest.get(uri"$basePath/v7/finance/download/$symbol_id?interval=$interval&period1=$from_date&period2=$to_date")
        .send(backend)
        .body
        .fold(err => Left(new RuntimeException(err)), x => Right(x))
    }.flatMap(IO.fromEither(_))
}

object SttpQuery1ApiPClient {
  def apply(basePath: String): SttpQuery1ApiPClient =
    SttpQuery1ApiPClient(basePath, HttpURLConnectionBackend())

  def apply(basePath: String, backend: SttpBackend[Identity, Any]): SttpQuery1ApiPClient =
    new SttpQuery1ApiPClient(basePath, backend)
}