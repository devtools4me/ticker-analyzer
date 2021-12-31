package me.devtools4.ticker.listing.model

import akka.actor.{Actor, Props}
import co.alphavantage.OverviewResponse
import co.alphavantage.service.AVantageQueryService
import me.devtools4.ticker.listing.model.TickerActor.{GetOverviewCmd, OverviewErrorEvent, OverviewPendingEvent, OverviewReadyEvent}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class TickerActor(private val symbol: String, private val service: AVantageQueryService) extends Actor {
  override def receive: Receive = idle

  implicit val ec = ExecutionContext.global

  def idle: Receive = {
    case GetOverviewCmd =>
      context.become(receiving)
      Future {
        service.companyOverview(symbol)
      }.onComplete {
        case Success(res) if OverviewResponse.isValid(res) => self ! OverviewReadyEvent(res)
        case Success(res) => self ! OverviewErrorEvent(new IllegalArgumentException(s"symbol=$symbol"))
        case Failure(err) => self ! OverviewErrorEvent(err)
      }
      sender() ! OverviewPendingEvent
  }

  def receiving: Receive = {
    case GetOverviewCmd => sender() ! OverviewPendingEvent
    case OverviewReadyEvent(overview) =>
      context.become(received(overview))
    case OverviewErrorEvent(err) =>
      context.become(error(err))
  }

  def received: OverviewResponse => Receive = overview => {
    case GetOverviewCmd => sender() ! OverviewReadyEvent(overview)
  }

  def error: Throwable => Receive = err => {
    case GetOverviewCmd => sender() ! OverviewErrorEvent(err)
  }
}

object TickerActor {
  def props(symbol: String, service: AVantageQueryService): Props =
    Props(new TickerActor(symbol, service))

  case object GetOverviewCmd

  sealed trait OverviewEvent
  case object OverviewPendingEvent extends OverviewEvent
  case class OverviewReadyEvent(overview : OverviewResponse) extends OverviewEvent
  case class OverviewErrorEvent(err : Throwable) extends OverviewEvent
}