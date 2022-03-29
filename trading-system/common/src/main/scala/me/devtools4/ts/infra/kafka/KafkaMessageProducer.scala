package me.devtools4.ts.infra.kafka

import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.{ExecutionContext, Future, Promise}

class KafkaMessageProducer[K, V](kafka: KafkaProducer[K, V])(implicit executor: ExecutionContext) extends LazyLogging {
  def send(t: String, k: K, v: V): Future[RecordMetadata] = {
    logger.info(s"send, t=$t, k=$k, v=$v")
    val rec = new ProducerRecord[K, V](t, k, v)
    val promise = Promise[RecordMetadata]()
    kafka.send(rec, (m: RecordMetadata, e: Exception) => {
      if (m != null) {
        logger.info(s"success, m=$m")
        promise.success(m)
      } else {
        logger.warn(s"failure, e=$e")
        promise.failure(e)
      }
      ()
    })
    promise.future
  }
}

object KafkaMessageProducer {
  def apply[K, V](kafka: KafkaProducer[K, V])(implicit executor: ExecutionContext) =
    new KafkaMessageProducer[K, V](kafka)
}