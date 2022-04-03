package me.devtools4.ts.orderbook.repository

import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import me.devtools4.ts.dto.{OrderBookStartedEvent, Ticker, Version}
import me.devtools4.ts.orderbook.domain.OrderBookAggregate
import me.devtools4.ts.orderbook.model.OrderBookEventEntity
import org.flywaydb.core.Flyway
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scalikejdbc.{ConnectionPool, GlobalSettings, LoggingSQLAndTimeSettings}

import java.time.ZonedDateTime

@RunWith(classOf[JUnitRunner])
class OrderBookEventStoreRepositorySpec extends AnyFlatSpec
  with BeforeAndAfterAll
  with ForAllTestContainer {
  override val container: PostgreSQLContainer = PostgreSQLContainer(
    dockerImageNameOverride = "postgres:14.2"
  )

  private var sut: OrderBookEventStoreRepository = _

  override def beforeAll(): Unit = {
    val driver = container.driverClassName
    val url = container.jdbcUrl
    val user = container.username
    val password = container.password

    Class.forName(driver)
    ConnectionPool.singleton(url, user, password)
    GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
      enabled = true,
      singleLineMode = true,
      logLevel = Symbol("debug").name
    )

    val ds = ConnectionPool.get().dataSource
    val fw = Flyway.configure().dataSource(ds).load()
    fw.migrate()

    sut = OrderBookEventStoreRepository()
  }

  it should "do something" in {
    assertResult(sut.findAll())(List())

    val event = OrderBookStartedEvent(Ticker("AAPL"), Version().nextVersion)
    val saved = sut.save(OrderBookEventEntity(
      ZonedDateTime.now(),
      "AAPL",
      OrderBookAggregate.getClass.getTypeName,
      Version().nextVersion.value,
      event.getClass.getTypeName,
      event))
    assertResult(saved.isDefined)(true)

    val found = sut.findByAggregateId("AAPL")
    assertResult(found.isEmpty)(false)

    val all = sut.findAll()
    assertResult(found.isEmpty)(false)
  }
}