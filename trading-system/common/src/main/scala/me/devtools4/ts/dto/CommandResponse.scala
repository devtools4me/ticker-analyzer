package me.devtools4.ts.dto

import upickle.default.{ReadWriter, macroRW}

sealed trait CommandResponse

object CommandResponse {
  implicit val rw: ReadWriter[CommandResponse] = macroRW
}

case class SuccessResponse(message: String) extends CommandResponse

object SuccessResponse {
  implicit val rw: ReadWriter[SuccessResponse] = macroRW
}

case class ErrorResponse(error: String) extends CommandResponse

object ErrorResponse {
  implicit val rw: ReadWriter[ErrorResponse] = macroRW
}