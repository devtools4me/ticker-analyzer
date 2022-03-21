package me.devtools4.ts

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import me.devtools4.ts.dto.{OrderBookStartedEvent, Version}
import me.devtools4.ts.orderbook.domain.OrderBookAggregate
import me.devtools4.ts.orderbook.model.OrderBookEventEntity
import me.devtools4.ts.orderbook.repository.OrderBookEventStoreRepository
import org.flywaydb.core.Flyway
import scalikejdbc.{ConnectionPool, GlobalSettings, LoggingSQLAndTimeSettings}

import java.time.ZonedDateTime

object EmbeddedPostgresRun extends App {
  val db = EmbeddedPostgres.builder()
    .start()
  System.out.println(s"host=${db.getHost}, port=${db.getPort}")

  val ds = db.getPostgresDatabase()
  val fw = Flyway.configure().dataSource(ds).load()
  fw.migrate()

  Class.forName("org.postgresql.Driver")
  ConnectionPool.singleton(db.getJdbcUrl("postgres"), "postgres", "postgres")
  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = Symbol("debug").name
  )

  val repo = OrderBookEventStoreRepository()
  val list = repo.findAll()
  System.out.println(s"list = $list")

  val event = OrderBookStartedEvent("", "", Version().nextVersion)
  val saved = repo.save(OrderBookEventEntity(1L,
    ZonedDateTime.now(),
    "TEST",
    OrderBookAggregate.getClass.getTypeName,
    Version().nextVersion.value,
    event.getClass.getTypeName,
    event))
  System.out.println(s"saved = $saved")

  val found = repo.findByAggregateId("TEST")
  System.out.println(s"found = $found")

  val all = repo.findAll()
  System.out.println(s"all = $all")

  db.close();
}
