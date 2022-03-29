package me.devtools4.ts.orderbook.infra

import com.typesafe.scalalogging.LazyLogging
import me.devtools4.ts.dto.{OrderBookEvent, Version}
import me.devtools4.ts.event.{EventProducer, EventStore, EventStoreRepository}
import me.devtools4.ts.orderbook.domain.OrderBookAggregate
import me.devtools4.ts.orderbook.model.OrderBookEventEntity
import org.apache.kafka.clients.producer.RecordMetadata

import java.time.ZonedDateTime
import java.util.ConcurrentModificationException
import scala.concurrent.Future

class OrderBookEventStore(repository: EventStoreRepository[OrderBookEventEntity],
                          producer: EventProducer[OrderBookEvent, Future[RecordMetadata]])
  extends EventStore[OrderBookEvent] with LazyLogging  {
  override def save(aggregateId: String, events: List[OrderBookEvent], expectedVersion: Version): Unit = {
    logger.info(s"save, aggregateId=$aggregateId, events=$events, expectedVersion=$expectedVersion")
    if (expectedVersion.isDefined) {
      repository.findByAggregateId(aggregateId)
        .lastOption
        .map(e => e.version)
        .filter(x => expectedVersion.value == x)
        .getOrElse(() => throw new ConcurrentModificationException())
    }
    events.foldLeft(expectedVersion.value) { (v, e) =>
      val newVersion = v + 1
      repository.save(OrderBookEventEntity(
        ZonedDateTime.now(),
        aggregateId,
        OrderBookAggregate.getClass.getTypeName,
        newVersion,
        e.getClass.getTypeName,
        e)
      ).map(_.eventData).foreach(producer.send)

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
            producer: EventProducer[OrderBookEvent, Future[RecordMetadata]]) =
    new OrderBookEventStore(repository, producer)
}
