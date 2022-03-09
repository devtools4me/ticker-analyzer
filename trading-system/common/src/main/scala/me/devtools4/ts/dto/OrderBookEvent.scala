package me.devtools4.ts.dto

sealed trait OrderBookEvent {
  def ticker: Ticker
  def version: Version
}

case class OrderBookStartedEvent(id: String, ticker: Ticker, version: Version) extends OrderBookEvent

case class OrderSubmittedEvent(order: Order, ticker: Ticker, version: Version) extends OrderBookEvent

case class TradeMatchedEvent(trades: List[Trade], ticker: Ticker, version: Version) extends OrderBookEvent

case class OrderBookStoppedEvent(ticker: Ticker, version: Version) extends OrderBookEvent