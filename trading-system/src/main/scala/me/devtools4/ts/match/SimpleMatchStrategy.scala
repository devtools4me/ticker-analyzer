package me.devtools4.ts.`match`
import me.devtools4.ts.api

class SimpleMatchStrategy extends MatchStrategy {
  override def matchOrder(book: OrderBook, o: api.Order): List[api.Trade] = ???
}

object SimpleMatchStrategy {
  def apply() = new SimpleMatchStrategy
}