package me.devtools4.ts.domain

import me.devtools4.ts.`match`.{MatchStrategy, OrderBook, OrderContainer}
import me.devtools4.ts.api._

class TradeEngineAggregate(override var id: String,
                           symbol: String,
                           strategies: Map[OrderType, MatchStrategy]) extends AggregateRoot[TradeEngineEvent] {

  private val book: OrderBook = OrderBook(new OrderContainer(), new OrderContainer())

  riseEvent(TradeEngineStartedEvent(id, symbol))

  def submitOrder(o: Order): Unit = riseEvent(OrderSubmittedEvent(o))

  def stop(): Unit = riseEvent(TradeEngineStoppedEvent)

  override protected def apply(e: TradeEngineEvent): Unit = e match {
    case TradeEngineStartedEvent(i, s) =>
    case OrderSubmittedEvent(o) => strategies.get(o.orderType)
      .map(x => x.matchOrder(book, o))
      .map(TradeMatchedEvent)
      .foreach(riseEvent(_))
    case TradeMatchedEvent(trades) => ???
    case TradeEngineStoppedEvent =>
  }
}

object TradeEngineAggregate {
  def apply(id: String, symbol: String, strategies: Map[OrderType, MatchStrategy]) =
    new TradeEngineAggregate(id, symbol, strategies)
}
