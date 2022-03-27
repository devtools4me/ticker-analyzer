package me.devtools4.ts.orderbook

import me.devtools4.ts.cmd.CommandDispatcher
import me.devtools4.ts.config.{DbConfig, ServiceConfig}
import me.devtools4.ts.dto.OrderBookCommand
import me.devtools4.ts.infra.kafka.KafkaMessageProducer
import me.devtools4.ts.orderbook.domain.OrderBookAggregate
import me.devtools4.ts.orderbook.infra.{OrderBookCommandHandler, OrderBookEventProducer, OrderBookEventSourcingHandler, OrderBookEventStore}
import me.devtools4.ts.orderbook.repository.OrderBookEventStoreRepository
import org.apache.kafka.clients.producer.KafkaProducer
import org.flywaydb.core.Flyway
import scalikejdbc.{ConnectionPool, GlobalSettings, LoggingSQLAndTimeSettings}

import java.nio.ByteBuffer
import scala.concurrent.ExecutionContext

class AppContext(conf: ServiceConfig)(implicit executor: ExecutionContext)  {
  lazy val kafkaProducer: KafkaProducer[String, ByteBuffer] = kafkaProducer(conf.kafka)
  lazy val messageProducer = KafkaMessageProducer[String, ByteBuffer](kafkaProducer)
  lazy val eventProducer = OrderBookEventProducer("order.book", messageProducer)

  lazy val eventStoreRepository: OrderBookEventStoreRepository = eventStoreRepository(conf.db)

  lazy val eventStore = OrderBookEventStore(eventStoreRepository, eventProducer)
  lazy val eventSrcHandler = OrderBookEventSourcingHandler(eventStore,
    OrderBookAggregate("", "", OrderBookAggregate.map)
  )
  lazy val cmdHandler = OrderBookCommandHandler(eventSrcHandler)
  lazy val cmdDispatcher = CommandDispatcher[OrderBookCommand](cmdHandler)

  private def kafkaProducer(conf: Map[String, String]) = {
    import me.devtools4.ts.config.Ops._
    new KafkaProducer[String, ByteBuffer](conf.properties)
  }

  private def eventStoreRepository(conf: DbConfig) = {
    Class.forName(conf.driver)
    ConnectionPool.singleton(conf.url, conf.user, conf.password)

    val ds = ConnectionPool.get().dataSource
    val fw = Flyway.configure().dataSource(ds).load()
    fw.migrate()

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