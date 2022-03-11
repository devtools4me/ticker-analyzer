package me.devtools4.ts.dto

import upickle.default.{ReadWriter, macroRW}

sealed trait OrderType
case object SimpleOrderType extends OrderType
case object IcebergOrderType extends OrderType

sealed trait Order {
  def id: String

  def orderType: OrderType

  def side: Side

  def price: PriceType

  def volume: VolumeType

  def time: Long
}

object Order {
  implicit val rw: ReadWriter[Order] = macroRW
}

case class SimpleOrder(id: String, side: Side, price: BigDecimal, volume: Long, time: Long) extends Order {
  override def orderType: OrderType = SimpleOrderType
}

object SimpleOrder {
  implicit val rw: ReadWriter[SimpleOrder] = macroRW
}

case class IcebergOrder(id: String, side: Side, price: BigDecimal, volume: Long, time: Long) extends Order {
  override def orderType: OrderType = IcebergOrderType
}

object IcebergOrder {
  implicit val rw: ReadWriter[IcebergOrder] = macroRW
}

case class BidAsk(bid: Order, ask: Order) {
  def isTradable: Boolean = bid.price.compareTo(ask.price) >= 0
}

