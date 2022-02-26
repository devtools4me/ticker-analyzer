package me.devtools4.ts.dto

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

case class SimpleOrder(id: String, side: Side, price: BigDecimal, volume: Long, time: Long) extends Order {
  override def orderType: OrderType = SimpleOrderType
}

case class IcebergOrder(id: String, side: Side, price: BigDecimal, volume: Long, time: Long) extends Order {
  override def orderType: OrderType = IcebergOrderType
}

