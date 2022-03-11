package me.devtools4.ts

import upickle.default.{ReadWriter, macroRW}

package object dto {

  type PriceType = BigDecimal
  type VolumeType = Long
  type TimeType = Long
  type Ticker = String
  type Side = String

  val Bid: Side = "B"
  val Ask: Side = "S"

  case class Version(value: Int = -1) extends AnyVal {
    def isDefined: Boolean = value > -1

    def nextVersion: Version = Version(value + 1)
  }

  object Version {
    implicit val rw: ReadWriter[Version] = macroRW
  }
}
