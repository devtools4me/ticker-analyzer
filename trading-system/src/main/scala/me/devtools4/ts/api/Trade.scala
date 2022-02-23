package me.devtools4.ts.api

case class Trade(bidId: String,
                 askId: String,
                 price: PriceType,
                 volume: VolumeType,
                 time: TimeType)
