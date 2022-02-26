package me.devtools4.ts.dto

sealed trait OrderBookCommand

case class StartCommand(id: String, symbol: String) extends OrderBookCommand

case class BidCommand(id: String,
                      price: PriceType,
                      volume: VolumeType,
                      time: TimeType) extends OrderBookCommand

case class AskCommand(id: String,
                      price: PriceType,
                      volume: VolumeType,
                      time: TimeType) extends OrderBookCommand

case object StopCommand extends OrderBookCommand