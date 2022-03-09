package me.devtools4.ts.orderbook.infra

import me.devtools4.ts.dto.OrderBookEvent
import me.devtools4.ts.event.EventProducer
import me.devtools4.ts.infra.kafka.KafkaMessageProducer
import upickle.default.ReadWriter

class OrderBookEventProducer(topic: String, mp: KafkaMessageProducer[String, Array[Byte]]) extends EventProducer[OrderBookEvent] {
  //implicit val rw: ReadWriter[OrderBookEvent] = upickle.default.macroRW[OrderBookEvent]
  override def send(event: OrderBookEvent): Unit = {
    val json = ""//upickle.default.write(event)
    mp.send(topic, event.ticker.symbol, json.getBytes)
  }
}

object OrderBookEventProducer {
  def apply(topic: String, mp: KafkaMessageProducer[String, Array[Byte]]): OrderBookEventProducer =
    new OrderBookEventProducer(topic, mp)
}