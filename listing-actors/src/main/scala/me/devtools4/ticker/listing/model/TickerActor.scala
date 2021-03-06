package me.devtools4.ticker.listing.model

import akka.actor.SupervisorStrategy.{Resume, Stop}
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props}
import akka.pattern.{Backoff, BackoffSupervisor}
import co.alphavantage.OverviewResponse
import co.alphavantage.service.AVantageQueryService
import me.devtools4.ticker.listing.model.TickerActor.{GetOverviewCmd, OverviewErrorEvent, OverviewPendingEvent, OverviewReadyEvent}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class TickerActor(private val symbol: String, private val service: AVantageQueryService) extends Actor with ActorLogging {

  implicit val ec = ExecutionContext.global

  override def receive: Receive = {
    case m @ GetOverviewCmd =>
      log.info("Received message={}", m)
      context.become(receiving)
      Future {
        service.companyOverview(symbol)
      }.onComplete {
        case Success(res) if OverviewResponse.isValid(res) =>
          log.info("Received valid overview, symbol={}, response={}", symbol, res)
          self ! OverviewReadyEvent(res)
        case Success(res) =>
          log.warning("Received invalid overview, symbol={}", symbol)
          self ! OverviewErrorEvent(new IllegalArgumentException(s"symbol=$symbol"))
        case Failure(err) =>
          log.error(err, "Request failed, error={}", err.getCause)
          self ! OverviewErrorEvent(err)
      }
      sender() ! OverviewPendingEvent
  }

  def receiving: Receive = {
    case m @ GetOverviewCmd =>
      log.info("Received message={}", m)
      sender() ! OverviewPendingEvent
    case m @ OverviewReadyEvent(overview) =>
      log.info("Received message={}", m)
      context.become(received(overview))
    case m @ OverviewErrorEvent(err) =>
      log.info("Received message={}", m)
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

  def propsWithSupervisor(symbol: String, service: AVantageQueryService): Props = BackoffSupervisor.props(
    Backoff.onFailure(
      Props(new TickerActor(symbol, service)),
      symbol,
      3 seconds, 30 seconds,
      0.2
    ).withSupervisorStrategy(
      OneForOneStrategy() {
        case _ => Resume
      }
    )
  )

  case object GetOverviewCmd

  sealed trait OverviewEvent
  case object OverviewPendingEvent extends OverviewEvent
  case class OverviewReadyEvent(overview : OverviewResponse) extends OverviewEvent
  case class OverviewErrorEvent(err : Throwable) extends OverviewEvent
}