package me.devtools4.ts.service

import me.devtools4.ts.api._
import me.devtools4.ts.domain._

class TradeEngineCommandHandler(handler: EventSourcingHandler[TradeEngineEvent, TradeEngineAggregate]
                               ) extends CommandHandler[TradeEngineCommand] {
  override def handle(cmd: TradeEngineCommand): Unit = cmd match {
    case c @ StartCommand(_, _) =>
      val a = TradeEngineAggregate(c)
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