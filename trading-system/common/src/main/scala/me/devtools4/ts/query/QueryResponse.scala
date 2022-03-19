package me.devtools4.ts.query

import upickle.default.{ReadWriter, macroRW}

sealed trait QueryResponse

object QueryResponse {
  implicit val rw: ReadWriter[QueryResponse] = macroRW
}

case class SuccessQueryResponse(message: String) extends QueryResponse

object SuccessQueryResponse {
  implicit val rw: ReadWriter[SuccessQueryResponse] = macroRW
}

case class ErrorQueryResponse(error: String) extends QueryResponse

object ErrorQueryResponse {
  implicit val rw: ReadWriter[ErrorQueryResponse] = macroRW
}