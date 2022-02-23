package me.devtools4.ts.service

import me.devtools4.ts.api.{EventStore, TradeEngineEvent}
import me.devtools4.ts.domain.{AggregateRoot, EventSourcingHandler, TradeEngineAggregate}

class TradeEngineEventSourcingHandler(store: EventStore[TradeEngineEvent], func: => TradeEngineAggregate)
  extends EventSourcingHandler[TradeEngineEvent, TradeEngineAggregate] {
  override def save(aggregate: AggregateRoot[TradeEngineEvent]): Unit = {
    store.save(aggregate.getId, aggregate.uncommittedChanges, aggregate.getVersion)
    aggregate.markChangesAsCommitted()
  }

  override def find(id: String): TradeEngineAggregate = {
    val aggregate = func
    val events = store.find(id)
    aggregate.replyEvents(events)
    events.lastOption.foreach(x => aggregate.setVersion(x.version))
    aggregate
  }
}
