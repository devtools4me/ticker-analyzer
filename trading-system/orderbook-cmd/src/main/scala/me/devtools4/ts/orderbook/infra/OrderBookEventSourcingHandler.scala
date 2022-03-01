package me.devtools4.ts.orderbook.infra

import me.devtools4.ts.domain.{AggregateRoot, EventSourcingHandler}
import me.devtools4.ts.dto.OrderBookEvent
import me.devtools4.ts.event.EventStore
import me.devtools4.ts.orderbook.domain.OrderBookAggregate

class OrderBookEventSourcingHandler(store: EventStore[OrderBookEvent], func: => OrderBookAggregate)
  extends EventSourcingHandler[OrderBookEvent, OrderBookAggregate] {
  override def save(aggregate: AggregateRoot[OrderBookEvent]): Unit = {
    store.save(aggregate.getId, aggregate.uncommittedChanges, aggregate.getVersion)
    aggregate.markChangesAsCommitted()
  }

  override def find(id: String): OrderBookAggregate = {
    val aggregate = func
    val events = store.find(id)
    aggregate.replyEvents(events)
    events.lastOption.foreach(x => aggregate.setVersion(x.version))
    aggregate
  }
}

object OrderBookEventSourcingHandler {
  def apply(store: EventStore[OrderBookEvent], func: => OrderBookAggregate) =
    new OrderBookEventSourcingHandler(store, func)
}
