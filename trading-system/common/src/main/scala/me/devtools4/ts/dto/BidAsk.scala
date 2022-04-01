package me.devtools4.ts.dto

case class BidAsk(bid: Order, ask: Order) {
  def isTradable: Boolean = bid.price.compareTo(ask.price) >= 0

  def trade: Trade = ???

  def diff(t: Trade): BidAsk = ???

  def minusVolume(volume: VolumeType, time: TimeType): BidAsk = ???

  override def toString: String = s"$bid,$ask"
}