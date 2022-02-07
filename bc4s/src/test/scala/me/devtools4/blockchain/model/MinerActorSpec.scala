package me.devtools4.blockchain.model

import akka.actor.ActorSystem
import akka.testkit.{EventFilter, ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import me.devtools4.blockchain.model.MinerActor.{InitCmd, MinerReady}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

import scala.concurrent.duration._

class MinerActorSpec extends TestKit(ActorSystem(
  "MinerActorSpec",
  ConfigFactory.load() //.getConfig("minerActorSpec")
))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A miner actor" should {
    "do this" in {
      val actor = system.actorOf(MinerActor.props())

      EventFilter.info(pattern = "Received message=MineBlockCmd", occurrences = 1) intercept {
        within(5 seconds) {
          actor ! InitCmd(MinerState())
          expectMsg(MinerReady)

          //          Thread.sleep(1000)
          //
          //          actor ! GetMinerStateCmd
          //          val e = expectMsgType[MinerStateEvent]
          //          assertResult(MinerStateEvent(MinerState()))(e)
          //
          ////          Thread.sleep(1000)
          //
          //          actor ! MineBlockCmd(Block(
          //            1L,
          //            "Transaction",
          //            GENESIS_PREV_HASH,
          //          ))
          //
          //          val e2 = expectMsgType[MinerStateEvent]
        }
      }
    }
  }

}
