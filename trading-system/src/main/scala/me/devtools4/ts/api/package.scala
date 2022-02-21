package me.devtools4.ts

package object api {
  sealed trait Message {
    def id: String
  }

  case class Event(override val id: String, version: Int) extends Message

  case class Command(override val id: String) extends Message
  case class SubmitOrderCommand(order: Order) extends Command(order.id)

  sealed trait Side
  case object Bid extends Side
  case object Ask extends Side

  sealed trait Order {
    def id: String
    def side: Side
    def price: BigDecimal
    def volume: Long
    def time: Long
  }
  case class SimpleOrder(id: String, side: Side, price: BigDecimal, volume: Long, time: Long) extends Order
  case class IcebergOrder(id: String, side: Side, price: BigDecimal, volume: Long, time: Long) extends Order

  case class BidAsk(bid: Order, ask: Order) {

  }



}
