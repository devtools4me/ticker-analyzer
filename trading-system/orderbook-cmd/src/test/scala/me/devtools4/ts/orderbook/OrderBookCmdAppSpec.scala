package me.devtools4.ts.orderbook

import com.dimafeng.testcontainers.{ForAllTestContainer, KafkaContainer, MultipleContainers, PostgreSQLContainer}
import me.devtools4.ts.config.{DbConfig, ServiceConfig}
import me.devtools4.ts.dto.BidCommand
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner

import scala.concurrent.ExecutionContext

@RunWith(classOf[JUnitRunner])
class OrderBookCmdAppSpec extends AnyFlatSpec
  with BeforeAndAfterAll
  with ForAllTestContainer {
  private val postgresContainer = PostgreSQLContainer(
    dockerImageNameOverride = "postgres:14.2"
  )
  private val kafkaContainer = KafkaContainer()

  override val container: MultipleContainers = MultipleContainers(
    postgresContainer, kafkaContainer
  )

  private var sut: AppContext = _

  override def beforeAll(): Unit = {
    val conf = ServiceConfig(
      Map(
        "bootstrap.servers" -> kafkaContainer.bootstrapServers,
        "group.id" -> "consumer-tutorial",
        "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
        "value.serializer" -> "org.apache.kafka.common.serialization.ByteBufferSerializer"
      ),
      DbConfig(
        postgresContainer.driverClassName,
        postgresContainer.jdbcUrl,
        postgresContainer.username,
        postgresContainer.password,
        "debug"
      ))
    sut = AppContext(conf)(ExecutionContext.global)
  }

  it should "do something" in {
    sut.cmdDispatcher.send(BidCommand("AAPL", 10, 10, 10))
  }
}