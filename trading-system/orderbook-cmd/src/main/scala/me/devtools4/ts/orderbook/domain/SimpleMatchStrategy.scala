package me.devtools4.ts.orderbook.domain

import me.devtools4.ts.dto.{Order, Trade}

class SimpleMatchStrategy extends MatchStrategy {
  override def matchOrder(book: OrderBook, o: Order): List[Trade] = List()
}

object SimpleMatchStrategy {
  def apply() = new SimpleMatchStrategy
}
