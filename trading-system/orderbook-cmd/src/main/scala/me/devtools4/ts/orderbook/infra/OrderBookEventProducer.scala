package me.devtools4.ts.orderbook.infra

import me.devtools4.ts.dto.OrderBookEvent
import me.devtools4.ts.event.EventProducer
import me.devtools4.ts.infra.kafka.KafkaMessageProducer
import org.apache.kafka.clients.producer.RecordMetadata

import java.nio.ByteBuffer
import scala.concurrent.Future

class OrderBookEventProducer(topic: String, mp: KafkaMessageProducer[String, ByteBuffer])
  extends EventProducer[OrderBookEvent, Future[RecordMetadata]]
{
  override def send(event: OrderBookEvent): Future[RecordMetadata] = {
    val json = upickle.default.write(event)
    mp.send(topic, event.ticker.value, ByteBuffer.wrap(json.getBytes))
  }
}

object OrderBookEventProducer {
  def apply(topic: String, mp: KafkaMessageProducer[String, ByteBuffer]): OrderBookEventProducer =
    new OrderBookEventProducer(topic, mp)
}