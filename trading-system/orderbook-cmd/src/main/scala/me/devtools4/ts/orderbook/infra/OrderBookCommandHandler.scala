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
    case BidCommand(id, ticker, price, volume, time) =>
      val a = handler.find(id)
      a.submitOrder(SimpleOrder(id, ticker, Bid, price, volume, time))
      handler.save(a)
    case AskCommand(id, ticker, price, volume, time) =>
      val a = handler.find(id)
      a.submitOrder(SimpleOrder(id, ticker, Ask, price, volume, time))
      handler.save(a)
    case AmendCommand(_, _, _, _) => ???
    case DeleteCommand(_) => ???
    case StopCommand() => ???
  }
}

object OrderBookCommandHandler {
  def apply(handler: EventSourcingHandler[OrderBookEvent, OrderBookAggregate]) =
    new OrderBookCommandHandler(handler)
}
