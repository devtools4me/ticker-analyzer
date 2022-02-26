package me.devtools4.ts.orderbook.domain

import me.devtools4.ts.dto.{Order, Trade}

trait MatchStrategy {
  def matchOrder(book: OrderBook, o: Order): List[Trade]
}
