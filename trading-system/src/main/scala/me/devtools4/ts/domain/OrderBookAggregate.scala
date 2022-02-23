package me.devtools4.ts.domain

import me.devtools4.ts.`match`.{MatchStrategy, OrderBook, OrderContainer, SimpleMatchStrategy}
import me.devtools4.ts.api._

class OrderBookAggregate(override var id: String,
                         symbol: String,
                         strategies: Map[OrderType, MatchStrategy]) extends AggregateRoot[OrderBookEvent] {

  private val book: OrderBook = OrderBook(new OrderContainer(), new OrderContainer())

  riseEvent(OrderBookStartedEvent(id, symbol, getVersion.nextVersion))

  def submitOrder(o: Order): Unit = riseEvent(OrderSubmittedEvent(o, getVersion.nextVersion))

  def stop(): Unit = riseEvent(OrderBookStoppedEvent(getVersion.nextVersion))

  override protected def apply(e: OrderBookEvent): Unit = e match {
    case OrderBookStartedEvent(i, s, v) =>
    case OrderSubmittedEvent(o, v) => strategies.get(o.orderType)
      .map(x => x.matchOrder(book, o))
      .map(x => TradeMatchedEvent(x, v.nextVersion))
      .foreach(riseEvent(_))
    case TradeMatchedEvent(trades, v) => ???
    case OrderBookStoppedEvent(v) =>
  }
}

object OrderBookAggregate {
  def apply(id: String, symbol: String, strategies: Map[OrderType, MatchStrategy]) =
    new OrderBookAggregate(id, symbol, strategies)

  def apply(cmd: StartCommand): OrderBookAggregate = OrderBookAggregate(cmd.id,
    cmd.symbol,
    Map(SimpleOrderType -> SimpleMatchStrategy()))
}
