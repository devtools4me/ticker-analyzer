package me.devtools4.ts.orderbook.repository

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import me.devtools4.ts.config.DbConfig
import me.devtools4.ts.dto.{OrderBookStartedEvent, Version}
import me.devtools4.ts.orderbook.domain.OrderBookAggregate
import me.devtools4.ts.orderbook.model.OrderBookEventEntity
import org.flywaydb.core.Flyway
import scalikejdbc.{ConnectionPool, GlobalSettings, LoggingSQLAndTimeSettings}

import java.time.ZonedDateTime

object EmbeddedPostgresRun extends App {
  val db = EmbeddedPostgres.builder()
    .start()
  println(s"host=${db.getHost}, port=${db.getPort}, url=${db.getJdbcUrl("postgres")}")

  val ds = db.getPostgresDatabase()
  val fw = Flyway.configure().dataSource(ds).load()
  fw.migrate()

  import pureconfig._
  import pureconfig.generic.auto._

  val config = ConfigSource.resources("db.conf").load[DbConfig] match {
    case Right(conf) => conf.copy(url = db.getJdbcUrl("postgres"))
    case Left(error) => throw new Exception(error.toString())
  }

  Class.forName(config.driver)
  ConnectionPool.singleton(config.url, config.user, config.password)
  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = Symbol(config.logLevel).name
  )

  val repo = OrderBookEventStoreRepository()
  val list = repo.findAll()
  println(s"list = $list")

  val event = OrderBookStartedEvent("AAPL", Version().nextVersion)
  val saved = repo.save(OrderBookEventEntity(
    ZonedDateTime.now(),
    "AAPL",
    OrderBookAggregate.getClass.getTypeName,
    Version().nextVersion.value,
    event.getClass.getTypeName,
    event))
  println(s"saved = $saved")

  val found = repo.findByAggregateId("AAPL")
  println(s"found = $found")

  val all = repo.findAll()
  println(s"all = $all")

  db.close();
}