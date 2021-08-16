package com.yahoo.finanance.query1

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Error()

object Error {
  implicit val encoder: Encoder[Error] = deriveEncoder[Error]
  implicit val decoder: Decoder[Error] = deriveDecoder[Error]
}