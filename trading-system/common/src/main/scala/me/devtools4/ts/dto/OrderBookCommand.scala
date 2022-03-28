package me.devtools4.ts.dto

import upickle.default.{ReadWriter, macroRW}

sealed trait OrderBookCommand

object OrderBookCommand {
  implicit val rw: ReadWriter[OrderBookCommand] = macroRW
}

case class StartCommand(id: String, symbol: Ticker) extends OrderBookCommand

object StartCommand {
  implicit val rw: ReadWriter[StartCommand] = macroRW
}

case class BidCommand(id: String,
                      symbol: Ticker,
                      price: PriceType,
                      volume: VolumeType,
                      time: TimeType) extends OrderBookCommand

object BidCommand {
  implicit val rw: ReadWriter[BidCommand] = macroRW
}

case class AskCommand(id: String,
                      symbol: Ticker,
                      price: PriceType,
                      volume: VolumeType,
                      time: TimeType) extends OrderBookCommand

object AskCommand {
  implicit val rw: ReadWriter[AskCommand] = macroRW
}

case class AmendCommand(id: String,
                      price: PriceType,
                      volume: VolumeType,
                      time: TimeType) extends OrderBookCommand

object AmendCommand {
  implicit val rw: ReadWriter[AmendCommand] = macroRW
}

case class DeleteCommand(id: String) extends OrderBookCommand

object DeleteCommand {
  implicit val rw: ReadWriter[DeleteCommand] = macroRW
}

case class StopCommand() extends OrderBookCommand

object StopCommand {
  implicit val rw: ReadWriter[StopCommand] = macroRW
}