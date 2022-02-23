package me.devtools4.ts.service

import me.devtools4.ts.api.{EventStoreRepository, EventStore, TradeEngineEvent, Version}
import me.devtools4.ts.domain.TradeEngineAggregate
import me.devtools4.ts.model.TradeEngineEventEntity

import java.time.ZonedDateTime
import java.util.ConcurrentModificationException

class TradeEngineEventStore(repository: EventStoreRepository[TradeEngineEventEntity]) extends EventStore[TradeEngineEvent] {
  override def save(aggregateId: String, events: List[TradeEngineEvent], expectedVersion: Version): Unit = {
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
        TradeEngineEventEntity(
          -1, //TODO: DB ID
          ZonedDateTime.now(),
          aggregateId,
          TradeEngineAggregate.getClass.getTypeName,
          newVersion,
          e.getClass.getTypeName,
          e))
        .foreach(x => {
          //TODO: broadcast
        })

      newVersion
    }
  }

  override def find(aggregateId: String): List[TradeEngineEvent] = {
    repository.findByAggregateId(aggregateId)
      .map(_.eventData)
  }
}
