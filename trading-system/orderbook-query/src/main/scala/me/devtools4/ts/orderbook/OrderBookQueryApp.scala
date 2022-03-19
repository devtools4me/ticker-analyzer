package me.devtools4.ts.orderbook

import me.devtools4.ts.orderbook.infra.OrderBookEventHandler
import me.devtools4.ts.orderbook.infra.consumer.OrderBookEventConsumer
import me.devtools4.ts.orderbook.infra.streams.OrderBookStreamConfig
import me.devtools4.ts.orderbook.query.OrderQueryHandler
import me.devtools4.ts.orderbook.repository.OrderEntityRepository
import me.devtools4.ts.query.{FindAllOrderBooksQuery, FindOrdersByIdQuery, QueryHandler, QueryResponse, SuccessQueryResponse}
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
  private lazy val queryRepository = OrderEntityRepository()
  private lazy val queryHandler = OrderQueryHandler(queryRepository)

  @cask.get("/order/:id")
  def getById(id: Long): String = {
    val res = queryHandler.handle(FindOrdersByIdQuery(id))
      .map(x => SuccessQueryResponse(s"id=${x.id}"))
    upickle.default.write(res)
  }

  @cask.get("/orders")
  def getAll: String = {
    val res = queryHandler.handle(FindAllOrderBooksQuery())
      .map(x => SuccessQueryResponse(s"id=${x.id}"))
    upickle.default.write(res)
  }
}
