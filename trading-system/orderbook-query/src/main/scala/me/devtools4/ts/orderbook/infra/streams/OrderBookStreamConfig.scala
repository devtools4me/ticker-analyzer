package me.devtools4.ts.orderbook.infra.streams

import me.devtools4.ts.dto.OrderBookEvent
import me.devtools4.ts.orderbook.infra.consumer.OrderBookEventConsumer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed

class OrderBookStreamConfig(sb: StreamsBuilder, consumer: OrderBookEventConsumer) {
  def orderBookStream() = {
    sb.stream("order.book", Consumed.`with`(Serdes.String(), Serdes.ByteArray()))
      .mapValues(new String(_))
      .mapValues(upickle.default.read[OrderBookEvent](_))
      .peek((_: String, value: OrderBookEvent) => consumer.consume(value, ()))
  }
}

object OrderBookStreamConfig {
  def apply(sb: StreamsBuilder, consumer: OrderBookEventConsumer) =
    new OrderBookStreamConfig(sb, consumer)
}