package me.devtools4.ts.orderbook

import me.devtools4.ts.cmd.CommandDispatcher
import me.devtools4.ts.dto.{OrderBookCommand, OrderBookEvent}
import me.devtools4.ts.infra.kafka.{KafkaEventProducer, KafkaMessageProducer}
import me.devtools4.ts.orderbook.domain.OrderBookAggregate
import me.devtools4.ts.orderbook.infra.{OrderBookCommandHandler, OrderBookEventSourcingHandler, OrderBookEventStore}
import me.devtools4.ts.orderbook.repository.OrderBookEventStoreRepository
implicit import scala.concurrent.ExecutionContext.global

object OrderBookCmdApp extends cask.MainRoutes {
  private lazy val producer = KafkaMessageProducer[String, Array[Byte]](null)
  private lazy val eventProducer = KafkaEventProducer[OrderBookEvent]("order.book", producer)
  private lazy val eventStoreRepository = OrderBookEventStoreRepository()
  private lazy val eventStore = OrderBookEventStore(eventStoreRepository, eventProducer)
  private lazy val eventSrcHandler = OrderBookEventSourcingHandler(eventStore,
    OrderBookAggregate("", "", OrderBookAggregate.map)
  )
  private lazy val cmdHandler = OrderBookCommandHandler(eventSrcHandler)
  private lazy val cmdDispatcher = CommandDispatcher[OrderBookCommand](cmdHandler)

  @cask.get("/order")
  def get(): String = {
    "Hello World!"
  }

  @cask.post("/post")
  def add(request: cask.Request): String = {
    request.text().reverse
  }

  @cask.patch("/post")
  def update(request: cask.Request): String = {
    request.text().reverse
  }

  @cask.delete("/delete")
  def delete(request: cask.Request): String = {
    request.text().reverse
  }

  initialize()
}