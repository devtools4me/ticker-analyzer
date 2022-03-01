package me.devtools4.ts.orderbook

import me.devtools4.ts.cmd.CommandDispatcher
import me.devtools4.ts.dto.OrderBookCommand
import me.devtools4.ts.orderbook.domain.OrderBookAggregate
import me.devtools4.ts.orderbook.infra.{OrderBookCommandHandler, OrderBookEventSourcingHandler, OrderBookEventStore}

object OrderBookCmdApp extends cask.MainRoutes {
  lazy val eventStore = OrderBookEventStore()
  lazy val eventSrcHandler = OrderBookEventSourcingHandler(eventStore,
    OrderBookAggregate("", "", OrderBookAggregate.map)
  )
  lazy val cmdHandler = OrderBookCommandHandler(eventSrcHandler)
  lazy val cmdDispatcher = CommandDispatcher[OrderBookCommand](cmdHandler)

  @cask.get("/")
  def hello(): String = {
    "Hello World!"
  }

  @cask.post("/do-thing")
  def doThing(request: cask.Request): String = {
    request.text().reverse
  }

  initialize()
}