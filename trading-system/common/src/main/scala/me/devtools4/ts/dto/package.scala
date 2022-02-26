package me.devtools4.ts

package object dto {

  type PriceType = BigDecimal
  type VolumeType = Long
  type TimeType = Long

  class Version(val version: Int = -1) extends AnyVal {
    def isDefined: Boolean = version > -1
    def nextVersion: Version = new Version(version + 1)
  }

  sealed trait Side
  case object Bid extends Side
  case object Ask extends Side

  case class BidAsk(bid: Order, ask: Order) {
    def isTradable: Boolean = bid.price.compareTo(ask.price) >= 0
  }
}
