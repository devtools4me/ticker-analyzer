package me.devtools4.ts

package object api {

  type PriceType = BigDecimal
  type VolumeType = Long
  type TimeType = Long

  class Version(val version: Int = -1) extends AnyVal {
    def isDefined: Boolean = version > -1
    def nextVersion: Version = new Version(version + 1)
  }

  sealed trait Command

  case class StartMatchingCommand(id: String, symbol: String) extends Command

  case class BidCommand(id: String,
                        price: PriceType,
                        volume: VolumeType,
                        time: TimeType) extends Command

  case class AskCommand(id: String,
                        price: PriceType,
                        volume: VolumeType,
                        time: TimeType) extends Command

  case class SubmitOrderCommand(order: Order) extends Command

  case class BidAsk(bid: Order, ask: Order) {
    def isTradable: Boolean = bid.price.compareTo(ask.price) >= 0

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
}
