package me.devtools4.ts.orderbook.domain

import me.devtools4.ts.domain.AggregateRoot
import me.devtools4.ts.dto._

class OrderBookAggregate(override var id: String,
                         symbol: Ticker,
                         strategies: Map[OrderType, MatchStrategy]) extends AggregateRoot[OrderBookEvent] {

  private val book: OrderBook = OrderBook(new OrderContainer(), new OrderContainer())

  riseEvent(OrderBookStartedEvent(id, symbol, getVersion.nextVersion))

  def submitOrder(o: Order): Unit = riseEvent(OrderSubmittedEvent(o, symbol, getVersion.nextVersion))

  def stop(): Unit = riseEvent(OrderBookStoppedEvent(symbol, getVersion.nextVersion))

  override protected def apply(e: OrderBookEvent): Unit = e match {
    case OrderBookStartedEvent(i, s, v) =>
    case OrderSubmittedEvent(o, s, v) => strategies.get(o.orderType)
      .map(x => x.matchOrder(book, o))
      .map(x => TradeMatchedEvent(x, s, v.nextVersion))
      .foreach(riseEvent(_))
    case TradeMatchedEvent(trades, s, v) => ???
    case OrderBookStoppedEvent(s, v) =>
  }
}

object OrderBookAggregate {
  val map: Map[OrderType, MatchStrategy] = Map(SimpleOrderType -> SimpleMatchStrategy())

  def apply(id: String, symbol: Ticker, strategies: Map[OrderType, MatchStrategy]) =
    new OrderBookAggregate(id, symbol, strategies)

  def apply(cmd: StartCommand): OrderBookAggregate = {
    OrderBookAggregate(cmd.id,
      cmd.symbol,
      map)
  }
}

