package me.devtools4.ts.api

sealed trait TradeEngineEvent

case class TradeEngineStartedEvent(id: String, symbol: String) extends TradeEngineEvent

case class OrderSubmittedEvent(order: Order) extends TradeEngineEvent

case class TradeMatchedEvent(trades: List[Trade]) extends TradeEngineEvent

case object TradeEngineStoppedEvent extends TradeEngineEvent