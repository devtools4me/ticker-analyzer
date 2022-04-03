package me.devtools4.ts.orderbook

import com.dimafeng.testcontainers.{ForAllTestContainer, KafkaContainer, MultipleContainers, PostgreSQLContainer}
import com.typesafe.scalalogging.LazyLogging
import io.undertow.Undertow
import me.devtools4.ts.config.{DbConfig, ServiceConfig}
import me.devtools4.ts.dto.{BidCommand, SuccessResponse, Ticker}
import me.devtools4.ts.orderbook.client.OrderBookCmdClient
import me.devtools4.ts.orderbook.route.OrderBookCmdRoutes
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.junit.JUnitRunner

import java.util.UUID
import scala.concurrent.ExecutionContext

@RunWith(classOf[JUnitRunner])
class OrderBookCmdAppSpec extends AnyFlatSpec
  with BeforeAndAfterAll
  with ForAllTestContainer
  with LazyLogging {
  private val postgresContainer = PostgreSQLContainer(
    dockerImageNameOverride = "postgres:14.2"
  )
  private val kafkaContainer = KafkaContainer()

  override val container: MultipleContainers = MultipleContainers(
    postgresContainer, kafkaContainer
  )

  private var ctx: AppContext = _
  private var app: cask.Main = _
  private var server: Undertow = _
  private var client: OrderBookCmdClient = _

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
    ctx = AppContext(conf)(ExecutionContext.global)
    app = new cask.Main() {
      val allRoutes = Seq(OrderBookCmdRoutes(ctx.cmdDispatcher))
    }
    server = Undertow.builder
      .addHttpListener(8081, "localhost")
      .setHandler(app.defaultHandler)
      .build
    server.start()
    client = OrderBookCmdClient("http://localhost:8081")
  }

  "Bid order" should "be submitted" in {
    val sym = "AAPL"
    val r = client.send(BidCommand(UUID.randomUUID().toString, Ticker(sym), 10, 10, 10))
    assertResult(SuccessResponse("done"))(r)

    val events = ctx.eventStoreRepository.findByAggregateId(sym)
    logger.info(s"events=$events")

    assertResult(false)(events.isEmpty)
  }
}