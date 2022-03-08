package me.devtools4.ts.infra.kafka

import me.devtools4.ts.event.EventProducer
import upickle.default.ReadWriter

class KafkaEventProducer[E](topic: String, mp: KafkaMessageProducer[String, Array[Byte]]) extends EventProducer[E] {
  private implicit val rw: ReadWriter[E] = upickle.default.macroRW[E]

  override def send(event: E): Unit = {
    val json = upickle.default.write(event)
    mp.send(topic, null, json.getBytes)
  }
}

object KafkaEventProducer {
  def apply[E](topic: String, mp: KafkaMessageProducer[String, Array[Byte]]): KafkaEventProducer[E] =
    new KafkaEventProducer(topic, mp)
}