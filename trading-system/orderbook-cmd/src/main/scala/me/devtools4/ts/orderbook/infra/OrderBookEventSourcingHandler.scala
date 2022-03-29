package me.devtools4.ts.orderbook.infra

import com.typesafe.scalalogging.LazyLogging
import me.devtools4.ts.domain.{AggregateRoot, EventSourcingHandler}
import me.devtools4.ts.dto.OrderBookEvent
import me.devtools4.ts.event.EventStore
import me.devtools4.ts.orderbook.domain.OrderBookAggregate

class OrderBookEventSourcingHandler(store: EventStore[OrderBookEvent], func: String => OrderBookAggregate)
  extends EventSourcingHandler[OrderBookEvent, OrderBookAggregate] with LazyLogging {
  override def save(aggregate: AggregateRoot[OrderBookEvent]): Unit = {
    logger.info(s"save, aggregate=$aggregate")
    store.save(aggregate.getId, aggregate.uncommittedChanges, aggregate.getVersion)
    aggregate.markChangesAsCommitted()
  }

  override def find(id: String): OrderBookAggregate = {
    logger.info(s"find, id=$id")
    val aggregate = func(id)
    val events = store.find(id)
    aggregate.replyEvents(events)
    events.lastOption.foreach(x => aggregate.setVersion(x.version))
    aggregate
  }
}

object OrderBookEventSourcingHandler {
  def apply(store: EventStore[OrderBookEvent], func: String => OrderBookAggregate) =
    new OrderBookEventSourcingHandler(store, func)
}
