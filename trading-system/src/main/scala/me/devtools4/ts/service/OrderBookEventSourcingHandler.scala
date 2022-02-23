package me.devtools4.ts.service

import me.devtools4.ts.api.{EventStore, OrderBookEvent}
import me.devtools4.ts.domain.{AggregateRoot, EventSourcingHandler, OrderBookAggregate}

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
