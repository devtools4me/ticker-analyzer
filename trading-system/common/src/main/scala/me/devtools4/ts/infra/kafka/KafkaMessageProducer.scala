package me.devtools4.ts.infra.kafka

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.{ExecutionContext, Future, Promise}

class KafkaMessageProducer[K, V](kafka: KafkaProducer[K, V])(implicit executor: ExecutionContext) {
  def send(t: String, k: K, v: V): Future[RecordMetadata] = {
    val rec = new ProducerRecord[K, V](t, k, v)
    val promise = Promise[RecordMetadata]()
    Future {
      kafka.send(rec, (m: RecordMetadata, e: Exception) => {
        if (m != null) {
          promise.success(m)
        } else {
          promise.failure(e)
        }
        ()
      })
    }
    promise.future
  }
}

object KafkaMessageProducer {
  def apply[K, V](kafka: KafkaProducer[K, V])(implicit executor: ExecutionContext) =
    new KafkaMessageProducer[K, V](kafka)
}