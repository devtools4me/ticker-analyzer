package me.devtools4.ts.orderbook.repository

import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner
import scalikejdbc.{ConnectionPool, GlobalSettings, LoggingSQLAndTimeSettings}

@RunWith(classOf[JUnitRunner])
class OrderBookEventStoreRepositorySpec extends AnyFlatSpec with ForAllTestContainer {
  override def container: PostgreSQLContainer = PostgreSQLContainer()
  container.container.start()
  Thread.sleep(10000)

  it should "do something" in {
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

    val repo = OrderBookEventStoreRepository()

    assertResult(repo.findAll())(List())
  }
}