package me.devtools4.ts.api

sealed trait TradeEngineCommand

case class StartCommand(id: String, symbol: String) extends TradeEngineCommand

case class BidCommand(id: String,
                      price: PriceType,
                      volume: VolumeType,
                      time: TimeType) extends TradeEngineCommand

case class AskCommand(id: String,
                      price: PriceType,
                      volume: VolumeType,
                      time: TimeType) extends TradeEngineCommand

case object StopCommand extends TradeEngineCommand