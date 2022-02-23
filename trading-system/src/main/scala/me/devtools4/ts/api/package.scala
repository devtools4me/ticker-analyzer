package me.devtools4.ts

package object api {

  type PriceType = BigDecimal
  type VolumeType = Long
  type TimeType = Long

  class Version(val version: Int = -1) extends AnyVal {
    def isDefined: Boolean = version > -1
    def nextVersion: Version = new Version(version + 1)
  }

  case class BidAsk(bid: Order, ask: Order) {
    def isTradable: Boolean = bid.price.compareTo(ask.price) >= 0
  }

  trait CommandHandler[C] {
    def handle(cmd: C): Unit
  }

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
