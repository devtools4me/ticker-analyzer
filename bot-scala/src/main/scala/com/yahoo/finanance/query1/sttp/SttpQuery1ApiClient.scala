package com.yahoo.finanance.query1.sttp

import com.yahoo.finanance.query1.{Query1Api, QuoteResponseResponse}
import io.circe.parser._
import sttp.client3._

class SttpQuery1ApiClient(basePath: String, backend: SttpBackend[Identity, Any]) extends Query1Api {
  override def quote(symbol_id: String): Either[String, QuoteResponseResponse] = {
    basicRequest.get(uri"$basePath/v7/finance/quote?symbols=$symbol_id")
      .send(backend)
      .body
      .flatMap { json =>
        decode[QuoteResponseResponse](json) match {
          case Left(err) => Left(s"${err.toString}, json=$json")
          case Right(value) => Right(value)
        }
      }
  }

  override def download(symbol_id: String, interval: String, from_date: Long, to_date: Long): String = ???
}

object SttpQuery1ApiClient {
  def apply(basePath: String): SttpQuery1ApiClient = new SttpQuery1ApiClient(basePath, HttpURLConnectionBackend())
}