package me.devtools4.ts.dto

sealed trait OrderBookEvent {
  def version: Version
}

case class OrderBookStartedEvent(id: String, symbol: String, version: Version) extends OrderBookEvent

case class OrderSubmittedEvent(order: Order, version: Version) extends OrderBookEvent

case class TradeMatchedEvent(trades: List[Trade], version: Version) extends OrderBookEvent

case class OrderBookStoppedEvent(version: Version) extends OrderBookEvent