package me.devtools4.ts.dto

case class Trade(bidId: String,
                 askId: String,
                 price: PriceType,
                 volume: VolumeType,
                 time: TimeType)
