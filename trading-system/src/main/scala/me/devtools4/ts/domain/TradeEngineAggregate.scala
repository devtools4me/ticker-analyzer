package me.devtools4.ts.domain

import me.devtools4.ts.`match`.{MatchStrategy, OrderBook, OrderContainer, SimpleMatchStrategy}
import me.devtools4.ts.api._

class TradeEngineAggregate(override var id: String,
                           symbol: String,
                           strategies: Map[OrderType, MatchStrategy]) extends AggregateRoot[TradeEngineEvent] {

  private val book: OrderBook = OrderBook(new OrderContainer(), new OrderContainer())

  riseEvent(TradeEngineStartedEvent(id, symbol, getVersion.nextVersion))

  def submitOrder(o: Order): Unit = riseEvent(OrderSubmittedEvent(o, getVersion.nextVersion))

  def stop(): Unit = riseEvent(TradeEngineStoppedEvent(getVersion.nextVersion))

  override protected def apply(e: TradeEngineEvent): Unit = e match {
    case TradeEngineStartedEvent(i, s, v) =>
    case OrderSubmittedEvent(o, v) => strategies.get(o.orderType)
      .map(x => x.matchOrder(book, o))
      .map(x => TradeMatchedEvent(x, v.nextVersion))
      .foreach(riseEvent(_))
    case TradeMatchedEvent(trades, v) => ???
    case TradeEngineStoppedEvent(v) =>
  }
}

object TradeEngineAggregate {
  def apply(id: String, symbol: String, strategies: Map[OrderType, MatchStrategy]) =
    new TradeEngineAggregate(id, symbol, strategies)

  def apply(cmd: StartCommand): TradeEngineAggregate = TradeEngineAggregate(cmd.id,
    cmd.symbol,
    Map(SimpleOrderType -> SimpleMatchStrategy()))
}
