package me.devtools4.ts.orderbook.infra

import com.typesafe.scalalogging.LazyLogging
import me.devtools4.ts.domain.{AggregateRoot, EventSourcingHandler}
import me.devtools4.ts.dto.{OrderBookEvent, Ticker}
import me.devtools4.ts.event.EventStore
import me.devtools4.ts.orderbook.domain.OrderBookAggregate

class OrderBookEventSourcingHandler(store: EventStore[OrderBookEvent], func: Ticker => OrderBookAggregate)
  extends EventSourcingHandler[Ticker, OrderBookEvent, OrderBookAggregate] with LazyLogging {
  override def save(aggregate: AggregateRoot[Ticker, OrderBookEvent]): Unit = {
    logger.info(s"save, aggregate=$aggregate")
    store.save(aggregate.getId.value, aggregate.uncommittedChanges, aggregate.getVersion)
    aggregate.markChangesAsCommitted()
  }

  override def find(id: Ticker): OrderBookAggregate = {
    logger.info(s"find, id=$id")
    val aggregate = func(id)
    val events = store.find(id.value)
    aggregate.replyEvents(events)
    events.lastOption.foreach(x => aggregate.setVersion(x.version))
    aggregate
  }
}

object OrderBookEventSourcingHandler {
  def apply(store: EventStore[OrderBookEvent], func: Ticker => OrderBookAggregate) =
    new OrderBookEventSourcingHandler(store, func)
}
