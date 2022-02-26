package me.devtools4.ts.orderbook.infra

import me.devtools4.ts.cmd.CommandHandler
import me.devtools4.ts.domain._
import me.devtools4.ts.dto._
import me.devtools4.ts.orderbook.domain.OrderBookAggregate

class OrderBookCommandHandler(handler: EventSourcingHandler[OrderBookEvent, OrderBookAggregate]
                             ) extends CommandHandler[OrderBookCommand] {
  override def handle(cmd: OrderBookCommand): Unit = cmd match {
    case c @ StartCommand(_, _) =>
      val a = OrderBookAggregate(c)
      handler.save(a)
    case BidCommand(id, price, volume, time) =>
      val a = handler.find(id)
      a.submitOrder(SimpleOrder(id, Bid, price, volume, time))
      handler.save(a)
    case AskCommand(id, price, volume, time) =>
      val a = handler.find(id)
      a.submitOrder(SimpleOrder(id, Ask, price, volume, time))
      handler.save(a)
    case StopCommand => ???
  }
}
