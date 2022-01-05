package me.devtools4.ticker.listing.model

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import co.alphavantage.OverviewResponse
import co.alphavantage.service.AVantageQueryService
import me.devtools4.ticker.listing.model.TickerActor.{GetOverviewCmd, OverviewErrorEvent, OverviewPendingEvent}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class TickerActorSpec extends TestKit(ActorSystem("TickerActorSpec"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll
{
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "The thing bieng tested" should {
    "do this" in {
      val actor = system.actorOf(TickerActor
        .props(
          "aapl",
          new AVantageQueryService() {
            override def companyOverview(symbol: String): OverviewResponse = ???

            override def activeListing(): String = ???
          }
        ))
      actor ! GetOverviewCmd
      expectMsg(OverviewPendingEvent)

      Thread.sleep(1000)

      actor ! GetOverviewCmd
      val m = expectMsgType[OverviewErrorEvent]
    }
  }
}