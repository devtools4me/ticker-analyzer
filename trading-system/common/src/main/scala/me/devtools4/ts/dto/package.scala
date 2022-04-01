package me.devtools4.ts

import upickle.default.{ReadWriter, macroRW}

package object dto {

  type PriceType = BigDecimal
  type VolumeType = Long
  type TimeType = Long
  type Ticker = String

  sealed trait Side {
    val value: String
  }

  case object Bid extends Side {
    override val value: String = "B"
  }

  case object Ask extends Side {
    override val value: String = "S"
  }

  object Side {
    implicit val rw: ReadWriter[Side] = macroRW

    def apply(s: String): Side = s match {
      case "B" => Bid
      case "S" => Ask
      case _ => ???
    }

    def isValid(s: String): Boolean = Bid.value == s || Ask.value == s
  }

  case class Version(value: Int = -1) extends AnyVal {
    def isDefined: Boolean = value > -1

    def nextVersion: Version = Version(value + 1)
  }

  object Version {
    implicit val rw: ReadWriter[Version] = macroRW
  }
}
