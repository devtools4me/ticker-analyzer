package me.devtools4.ts.dto

import me.devtools4.ts.dto.Side.toSide
import me.devtools4.ts.dto.SimpleOrder.toSimpleOrder
import me.devtools4.ts.dto.Ticker.toTicker
import upickle.default.{ReadWriter, macroRW}

import java.text.NumberFormat
import java.util.Locale

sealed trait OrderType

case object SimpleOrderType extends OrderType

case object IcebergOrderType extends OrderType

sealed trait Order {
  def id: String

  def orderType: OrderType

  def ticker: Ticker

  def side: Side

  def price: PriceType

  def priceStr: String = String.format("%6d", price.intValue)

  def volume: VolumeType

  def volumeStr: String = NumberFormat.getNumberInstance(Locale.US).format(volume)

  def time: Long

  def isBid: Boolean = Bid == side

  def isAsk: Boolean = Ask == side

  def minusVolume(volume: VolumeType, time: TimeType): Option[Order]

  def str: String =
    if (isBid)
      String.format("%10s %6s", volumeStr, priceStr)
    else
      String.format("%6s %10s", priceStr, volumeStr)
}

object Order {
  implicit val rw: ReadWriter[Order] = macroRW

  def toOrders(list: List[String]): List[Order] = list.flatMap(toOrder)

  def toOrder(s: String): Option[Order] = toOrder(s, System.nanoTime())

  def toOrder(s: String, time: TimeType): Option[Order] = s.split(",").toList match {
    case List(id, sym, side, price, vol) => toSimpleOrder(id, sym, side, price, vol, time)
    case _ => None
  }

  val emptyStr: String = String.format("%10s %6s", "", "")
}

case class SimpleOrder(id: String, ticker: Ticker, side: Side, price: BigDecimal, volume: Long, time: Long) extends Order {
  override def orderType: OrderType = SimpleOrderType

  override def minusVolume(v: VolumeType, t: TimeType): Option[Order] = {
    val newVolume = volume - v
    if (newVolume >= 0) Some(copy(volume = newVolume)) else None
  }
}

object SimpleOrder {
  implicit val rw: ReadWriter[SimpleOrder] = macroRW

  def toSimpleOrder(id: String, ticker: String, side: String, price: String, volume: String, time: TimeType): Option[SimpleOrder] =
    for {
      t <- toTicker(ticker)
      s <- toSide(side)
    } yield SimpleOrder(id, t, s, BigDecimal(price), volume.toLong, time)
}

case class IcebergOrder(id: String, ticker: Ticker, side: Side, price: BigDecimal, volume: Long, time: Long) extends Order {
  override def orderType: OrderType = IcebergOrderType

  override def minusVolume(volume: VolumeType, time: TimeType): Option[Order] = ???
}

object IcebergOrder {
  implicit val rw: ReadWriter[IcebergOrder] = macroRW
}

