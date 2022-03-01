package me.devtools4.ts.orderbook.infra

import me.devtools4.ts.dto.{OrderBookEvent, Version}
import me.devtools4.ts.event.{EventProducer, EventStore, EventStoreRepository}
import me.devtools4.ts.orderbook.domain.OrderBookAggregate
import me.devtools4.ts.orderbook.model.OrderBookEventEntity

import java.time.ZonedDateTime
import java.util.ConcurrentModificationException

class OrderBookEventStore(repository: EventStoreRepository[OrderBookEventEntity],
                          producer: EventProducer[OrderBookEvent]) extends EventStore[OrderBookEvent] {
  override def save(aggregateId: String, events: List[OrderBookEvent], expectedVersion: Version): Unit = {
    if (expectedVersion.isDefined) {
      repository.findByAggregateId(aggregateId)
        .lastOption
        .map(e => e.version)
        .filter(x => expectedVersion.version == x)
        .getOrElse(() => throw new ConcurrentModificationException())
    }
    events.foldLeft(expectedVersion.version) { (v, e) =>
      val newVersion = v + 1
      repository.save(
        OrderBookEventEntity(
          -1, //TODO: DB ID
          ZonedDateTime.now(),
          aggregateId,
          OrderBookAggregate.getClass.getTypeName,
          newVersion,
          e.getClass.getTypeName,
          e))
        .map(_.eventData)
        .foreach(producer.send)

      newVersion
    }
  }

  override def find(aggregateId: String): List[OrderBookEvent] = {
    repository.findByAggregateId(aggregateId)
      .map(_.eventData)
  }
}

object OrderBookEventStore {
  def apply(repository: EventStoreRepository[OrderBookEventEntity],
            producer: EventProducer[OrderBookEvent]) =
    new OrderBookEventStore(repository, producer)
}
