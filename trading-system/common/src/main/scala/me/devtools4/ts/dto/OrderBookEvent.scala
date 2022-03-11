package me.devtools4.ts.dto

import upickle.default.{ReadWriter, macroRW}

sealed trait OrderBookEvent {
  def ticker: Ticker

  def version: Version
}

object OrderBookEvent {
  implicit val rw: ReadWriter[OrderBookEvent] = macroRW
}

case class OrderBookStartedEvent(id: String, ticker: Ticker, version: Version) extends OrderBookEvent

object OrderBookStartedEvent {
  implicit val rw: ReadWriter[OrderBookStartedEvent] = macroRW
}

case class OrderSubmittedEvent(order: Order, ticker: Ticker, version: Version) extends OrderBookEvent

object OrderSubmittedEvent {
  implicit val rw: ReadWriter[OrderSubmittedEvent] = macroRW
}

case class TradeMatchedEvent(trades: List[Trade], ticker: Ticker, version: Version) extends OrderBookEvent

object TradeMatchedEvent {
  implicit val rw: ReadWriter[TradeMatchedEvent] = macroRW
}

case class OrderBookStoppedEvent(ticker: Ticker, version: Version) extends OrderBookEvent

object OrderBookStoppedEvent {
  implicit val rw: ReadWriter[OrderBookStoppedEvent] = macroRW
}