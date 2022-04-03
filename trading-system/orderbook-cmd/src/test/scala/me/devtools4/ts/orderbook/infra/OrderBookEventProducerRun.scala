package me.devtools4.ts.orderbook.infra

import me.devtools4.ts.config.Ops._
import me.devtools4.ts.dto.{OrderBookStartedEvent, Ticker, Version}
import me.devtools4.ts.infra.kafka.KafkaMessageProducer
import org.apache.kafka.clients.producer.KafkaProducer
import pureconfig._

import java.nio.ByteBuffer
import scala.concurrent.ExecutionContext

object OrderBookEventProducerRun extends App {

  val conf = ConfigSource.resources("kafka.conf").load[Map[String, String]] match {
    case Right(conf) => conf
    case Left(error) => throw new Exception(error.toString())
  }

  val kafkaProducer = new KafkaProducer[String, ByteBuffer](conf.properties)
  implicit val global = ExecutionContext.global
  val messageProducer = KafkaMessageProducer[String, ByteBuffer](kafkaProducer)
  val eventProducer = OrderBookEventProducer("order.book", messageProducer)

  val f1 = eventProducer.send(OrderBookStartedEvent(Ticker("AAPL"), Version().nextVersion))
  for {
    r <- f1
  } yield {
    println(s"meta = $r")
  }

  Thread.sleep(1000)
}