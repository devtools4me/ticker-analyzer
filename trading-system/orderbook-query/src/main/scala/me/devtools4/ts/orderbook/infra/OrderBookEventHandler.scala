package me.devtools4.ts.orderbook.infra

import me.devtools4.ts.dto.{OrderBookEvent, OrderBookStartedEvent, OrderBookStoppedEvent, OrderSubmittedEvent, TradeMatchedEvent}
import me.devtools4.ts.event.EventHandler

class OrderBookEventHandler extends EventHandler[OrderBookEvent] {
  override def handle(event: OrderBookEvent): Unit = event match {
    case OrderBookStartedEvent(i, s, v) =>
    case OrderSubmittedEvent(o, s, v) =>
    case TradeMatchedEvent(trades, s, v) =>
    case OrderBookStoppedEvent(s, v) =>
  }
}

object OrderBookEventHandler {
  def apply() = new OrderBookEventHandler
}