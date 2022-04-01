package me.devtools4.ts.dto

import upickle.default.{ReadWriter, macroRW}

import java.text.NumberFormat
import java.util.Locale
import scala.util.{Failure, Success, Try}

sealed trait OrderType

case object SimpleOrderType extends OrderType

case object IcebergOrderType extends OrderType

sealed trait Order {
  def id: String

  def orderType: OrderType

  def side: Side

  def price: PriceType

  def priceStr: String = String.format("%6d", price.intValue)

  def volume: VolumeType

  def volumeStr: String = NumberFormat.getNumberInstance(Locale.US).format(volume)

  def time: Long

  def isBid: Boolean = Bid == side

  def isAsk: Boolean = Ask == side

  def minusVolume(volume: VolumeType, time: TimeType): Try[Order]

  def str: String =
    if (isBid)
      String.format("%10s %6s", volumeStr, priceStr)
    else
      String.format("%6s %10s", priceStr, volumeStr)
}

object Order {
  implicit val rw: ReadWriter[Order] = macroRW

  def apply(s: String): Order = apply(s, System.nanoTime())

  def apply(s: String, time: TimeType): Order = s.split(",").toList match {
    case List(id, sym, side, price, vol) if Side.isValid(side) =>
      SimpleOrder(id, sym, Side(side), BigDecimal(price), vol.toLong, time)
    case _ => ???
  }
}

case class SimpleOrder(id: String, ticker: Ticker, side: Side, price: BigDecimal, volume: Long, time: Long) extends Order {
  override def orderType: OrderType = SimpleOrderType

  override def minusVolume(v: VolumeType, t: TimeType): Try[Order] = {
    val newVolume = volume - v
    if (newVolume >= 0) Success(this.copy(volume = newVolume)) else Failure(new IllegalArgumentException())
  }
}

object SimpleOrder {
  implicit val rw: ReadWriter[SimpleOrder] = macroRW
}

case class IcebergOrder(id: String, ticker: Ticker, side: Side, price: BigDecimal, volume: Long, time: Long) extends Order {
  override def orderType: OrderType = IcebergOrderType

  override def minusVolume(volume: VolumeType, time: TimeType): Try[Order] = ???
}

object IcebergOrder {
  implicit val rw: ReadWriter[IcebergOrder] = macroRW
}

