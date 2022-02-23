package me.devtools4.ts

package object api {

  type PriceType = BigDecimal
  type VolumeType = Long
  type TimeType = Long

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

  trait EventEntityRepository[E] {
    def save(e: E): Unit
    def find(id: Long): Option[E]
    def findByAggregateId(aggregateId: String): List[E]
  }

  trait EventStore[E] {
    def saveEvents(aggregateId: String, events: List[E], expectedVersion: Int): Unit
    def getEvents(aggregateId: String): List[E]
  }
}
