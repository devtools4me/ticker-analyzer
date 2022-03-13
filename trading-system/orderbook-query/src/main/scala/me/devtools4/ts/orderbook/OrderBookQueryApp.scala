package me.devtools4.ts.orderbook

import me.devtools4.ts.orderbook.infra.OrderBookEventHandler
import me.devtools4.ts.orderbook.infra.consumer.OrderBookEventConsumer
import me.devtools4.ts.orderbook.infra.streams.OrderBookStreamConfig
import org.apache.kafka.streams.StreamsBuilder

import java.util.Properties

object OrderBookQueryApp extends cask.MainRoutes {
  val kafkaProps = new Properties()
  kafkaProps.put("bootstrap.servers", "localhost:9092")
  kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.ByteBufferSerializer")

  private lazy val sb = new StreamsBuilder()
  private lazy val eventHandler = OrderBookEventHandler()
  private lazy val eventConsumer = OrderBookEventConsumer(eventHandler)
  private lazy val streamConfig = OrderBookStreamConfig(sb, eventConsumer)

  @cask.get("/order/:id")
  def get(id: String): String = {
    "Hello World!"
  }
}
