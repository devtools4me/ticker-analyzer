package me.devtools4.ts

import me.devtools4.ts.dto.Version

package object event {

  trait EventHandler[E] {
    def handle(event: E): Unit
  }

  trait EventStoreRepository[E] {
    def save(e: E): Option[E]
    def find(id: Long): Option[E]
    def findByAggregateId(aggregateId: String): List[E]
  }

  trait EventStore[E] {
    def save(aggregateId: String, events: List[E], expectedVersion: Version): Unit
    def find(aggregateId: String): List[E]
  }

  trait EventProducer[E] {
    def send(event: E): Unit
  }

  trait EventConsumer[E] {
    def consume(event: E): Unit
  }
}
