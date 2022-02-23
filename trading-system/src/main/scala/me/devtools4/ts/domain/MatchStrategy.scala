package me.devtools4.ts.domain

import me.devtools4.ts.api.{Order, Trade}

trait MatchStrategy {
  def matchOrder(book: OrderBook, o: Order): List[Trade]
}
