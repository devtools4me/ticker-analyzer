package me.devtools4.blockchain.model

import akka.actor.{Actor, ActorLogging, Props}
import me.devtools4.blockchain.model.MinerActor._

class MinerActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case InitCmd(state) =>
      sender() ! MinerReady
      context.become(ready(state))
  }

  def ready: MinerState => Receive = state => {
    case m@GetMinerStateCmd =>
      log.info("received message={}", m)
      sender() ! MinerStateEvent(state)
    case m@MineBlockCmd(b) =>
      log.info("received message={}", m)
      //      val block = b.generateHash()
      //      log.info("block found b={}", block)
      sender() ! MinerStateEvent(state)
    //      sender() ! BlockFoundEvent(b)
    //      context.become(ready(state.change(REWARD, block)))
  }
}

object MinerActor {
  def props(): Props = Props(new MinerActor())

  sealed trait MinerCmd

  sealed trait MinerEvent

  case class InitCmd(state: MinerState)

  case class MineBlockCmd(b: Block) extends MinerCmd

  case class BlockFoundEvent(b: Block) extends MinerEvent

  case class MinerStateEvent(state: MinerState) extends MinerEvent

  case object GetMinerStateCmd extends MinerCmd

  case object MinerReady extends MinerEvent

}