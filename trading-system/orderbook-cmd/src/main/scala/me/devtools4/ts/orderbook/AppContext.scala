package me.devtools4.ts.orderbook

import me.devtools4.ts.cmd.CommandDispatcher
import me.devtools4.ts.config.{DbConfig, ServiceConfig}
import me.devtools4.ts.dto.OrderBookCommand
import me.devtools4.ts.infra.kafka.KafkaMessageProducer
import me.devtools4.ts.orderbook.domain.OrderBookAggregate
import me.devtools4.ts.orderbook.infra.{OrderBookCommandHandler, OrderBookEventProducer, OrderBookEventSourcingHandler, OrderBookEventStore}
import me.devtools4.ts.orderbook.repository.OrderBookEventStoreRepository
import org.apache.kafka.clients.producer.KafkaProducer
import scalikejdbc.{ConnectionPool, GlobalSettings, LoggingSQLAndTimeSettings}

import java.util.Properties
import scala.concurrent.ExecutionContext

class AppContext(conf: ServiceConfig)(implicit executor: ExecutionContext)  {
  lazy val kafkaProducer: KafkaProducer[String, Array[Byte]] = kafkaProducer(conf.kafka)
  lazy val messageProducer = KafkaMessageProducer[String, Array[Byte]](kafkaProducer)
  lazy val eventProducer = OrderBookEventProducer("order.book", messageProducer)

  lazy val eventStoreRepository: OrderBookEventStoreRepository = eventStoreRepository(conf.db)

  lazy val eventStore = OrderBookEventStore(eventStoreRepository, eventProducer)
  lazy val eventSrcHandler = OrderBookEventSourcingHandler(eventStore,
    OrderBookAggregate("", "", OrderBookAggregate.map)
  )
  lazy val cmdHandler = OrderBookCommandHandler(eventSrcHandler)
  lazy val cmdDispatcher = CommandDispatcher[OrderBookCommand](cmdHandler)

  private def kafkaProducer(conf: Map[String, String]) = {
    val props = conf.foldLeft(new Properties()) {
      (p, a) => {
        p.put(a._1, a._2)
        p
      }
    }
    new KafkaProducer[String, Array[Byte]](props)
  }

  private def eventStoreRepository(conf: DbConfig) = {
    Class.forName(conf.driver)
    ConnectionPool.singleton(conf.url, conf.user, conf.password)
    GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
      enabled = true,
      singleLineMode = true,
      logLevel = Symbol(conf.logLevel).name
    )
    OrderBookEventStoreRepository()
  }
}

object AppContext {
  def apply(conf: ServiceConfig)(implicit executor: ExecutionContext): AppContext =
    new AppContext(conf)
}