package me.devtools4.ts.orderbook.domain

import me.devtools4.ts.domain.AggregateRoot
import me.devtools4.ts.dto._

class OrderBookAggregate(override var id: String,
                         strategies: Map[OrderType, MatchStrategy]) extends AggregateRoot[OrderBookEvent] {

  private val book: OrderBook = OrderBook(new OrderContainer(), new OrderContainer())

  riseEvent(OrderBookStartedEvent(id, getVersion.nextVersion))

  def submitOrder(o: Order): Unit = riseEvent(OrderSubmittedEvent(o, id, getVersion.nextVersion))

  def stop(): Unit = riseEvent(OrderBookStoppedEvent(id, getVersion.nextVersion))

  override protected def apply(e: OrderBookEvent): Unit = e match {
    case OrderBookStartedEvent(i, v) =>
    case OrderSubmittedEvent(o, s, v) => strategies.get(o.orderType)
      .map(x => x.matchOrder(book, o))
      .map(x => TradeMatchedEvent(x, s, v.nextVersion))
      .foreach(riseEvent(_))
    case TradeMatchedEvent(List(), s, v) =>
    case TradeMatchedEvent(trades, s, v) =>
    case OrderBookStoppedEvent(s, v) =>
  }

  override def toString: String = {
    s"OrderBookAggregate($id, $book)"
  }
}

object OrderBookAggregate {
  val map: Map[OrderType, MatchStrategy] = Map(SimpleOrderType -> SimpleMatchStrategy())

  def apply(symbol: Ticker, strategies: Map[OrderType, MatchStrategy]) =
    new OrderBookAggregate(symbol, strategies)

  def apply(cmd: StartCommand): OrderBookAggregate = {
    OrderBookAggregate(cmd.symbol, map)
  }
}

