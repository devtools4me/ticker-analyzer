package me.devtools4.ts.orderbook.infra.consumer

import me.devtools4.ts.dto._
import me.devtools4.ts.event.{EventConsumer, EventHandler}

class OrderBookEventConsumer(eventHandler: EventHandler[OrderBookEvent]) extends EventConsumer[OrderBookEvent] {
  override def consume(event: OrderBookEvent, doneFunc: => Unit): Unit = {
    event match {
      case e @ OrderBookStartedEvent(id, sym, ver) =>
        eventHandler.handle(e)
      case e @ OrderSubmittedEvent(o, sym, ver) =>
        eventHandler.handle(e)
      case e @ TradeMatchedEvent(list, sym, ver) =>
        eventHandler.handle(e)
      case e @ OrderBookStoppedEvent(sym, ver) =>
        eventHandler.handle(e)
    }
    doneFunc
  }
}