package com.yahoo.finanance.query1

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class QuoteResponse(result: List[Quote], error: Option[Error])

object QuoteResponse {
  implicit val encoder: Encoder[QuoteResponse] = deriveEncoder[QuoteResponse]
  implicit val decoder: Decoder[QuoteResponse] = deriveDecoder[QuoteResponse]
}