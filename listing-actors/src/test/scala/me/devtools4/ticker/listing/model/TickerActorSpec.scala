package me.devtools4.ticker.listing.model

import akka.actor.ActorSystem
import akka.testkit.{EventFilter, ImplicitSender, TestKit}
import co.alphavantage.OverviewResponse
import co.alphavantage.service.AVantageQueryService
import com.typesafe.config.ConfigFactory
import me.devtools4.ticker.listing.model.TickerActor.{GetOverviewCmd, OverviewErrorEvent, OverviewPendingEvent}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

import scala.concurrent.duration._

class TickerActorSpec extends TestKit(ActorSystem(
  "TickerActorSpec",
  ConfigFactory.load().getConfig("tickerActorSpec")))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A ticker actor" should {
    "do this" in {
      val actor = system.actorOf(TickerActor
        .props(
          "aapl",
          new AVantageQueryService() {
            override def companyOverview(symbol: String): OverviewResponse = ???

            override def activeListing(): String = ???
          }
        ))

      EventFilter.info(pattern = "Received message=GetOverviewCmd", occurrences = 1) intercept {
        within(2 seconds) {
          actor ! GetOverviewCmd
          expectMsg(OverviewPendingEvent)

          Thread.sleep(1000)

          actor ! GetOverviewCmd
          val m = expectMsgType[OverviewErrorEvent]
        }
      }
    }
  }
}