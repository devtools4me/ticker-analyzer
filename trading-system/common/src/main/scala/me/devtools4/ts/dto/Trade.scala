package me.devtools4.ts.dto

import upickle.default.{ReadWriter, macroRW}

case class Trade(bidId: String,
                 askId: String,
                 price: PriceType,
                 volume: VolumeType,
                 time: TimeType)

object Trade {
  implicit val rw: ReadWriter[Trade] = macroRW
}
