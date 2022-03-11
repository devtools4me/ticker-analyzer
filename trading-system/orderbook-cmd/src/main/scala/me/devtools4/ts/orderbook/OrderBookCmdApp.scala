package me.devtools4.ts.orderbook

import me.devtools4.ts.cmd.CommandDispatcher
import me.devtools4.ts.dto.OrderBookCommand
import me.devtools4.ts.infra.kafka.KafkaMessageProducer
import me.devtools4.ts.orderbook.domain.OrderBookAggregate
import me.devtools4.ts.orderbook.infra.{OrderBookCommandHandler, OrderBookEventProducer, OrderBookEventSourcingHandler, OrderBookEventStore}
import me.devtools4.ts.orderbook.repository.OrderBookEventStoreRepository
import org.apache.kafka.clients.producer.KafkaProducer

import java.util.Properties

object OrderBookCmdApp extends cask.MainRoutes {
  val kafkaProps = new Properties()
  kafkaProps.put("bootstrap.servers", "localhost:9092")
  kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.ByteBufferSerializer")
  private lazy val kafkaProducer = new KafkaProducer[String, Array[Byte]](kafkaProps)
  private lazy val messageProducer = KafkaMessageProducer[String, Array[Byte]](kafkaProducer)
  private lazy val eventProducer = OrderBookEventProducer("order.book", messageProducer)
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
    //cmdDispatcher.send()
    request.text()
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