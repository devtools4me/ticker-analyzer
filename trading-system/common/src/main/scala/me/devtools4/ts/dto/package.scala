package me.devtools4.ts

import upickle.default.{ReadWriter, macroRW}

package object dto {

  type PriceType = BigDecimal
  type VolumeType = Long
  type TimeType = Long

  case class Ticker(value: String) extends AnyVal {}

  object Ticker {
    implicit val rw: ReadWriter[Ticker] = macroRW

    private val pattern = "[A-Z]".r

    def toTicker(s: String): Option[Ticker] = pattern.findFirstMatchIn(s).map(_ => Ticker(s))
  }

  case class Version(value: Int = -1) extends AnyVal {
    def isDefined: Boolean = value > -1

    def nextVersion: Version = Version(value + 1)
  }

  object Version {
    implicit val rw: ReadWriter[Version] = macroRW
  }
}
