package com.yahoo.finanance.query1

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class QuoteResponseResponse(quoteResponse: QuoteResponse)

object QuoteResponseResponse {
  implicit val encoder: Encoder[QuoteResponseResponse] = deriveEncoder[QuoteResponseResponse]
  implicit val decoder: Decoder[QuoteResponseResponse] = deriveDecoder[QuoteResponseResponse]
}