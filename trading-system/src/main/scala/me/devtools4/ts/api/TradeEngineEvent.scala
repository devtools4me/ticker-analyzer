package me.devtools4.ts.api

sealed trait TradeEngineEvent {
  def version: Version
}

case class TradeEngineStartedEvent(id: String, symbol: String, version: Version) extends TradeEngineEvent

case class OrderSubmittedEvent(order: Order, version: Version) extends TradeEngineEvent

case class TradeMatchedEvent(trades: List[Trade], version: Version) extends TradeEngineEvent

case class TradeEngineStoppedEvent(version: Version) extends TradeEngineEvent