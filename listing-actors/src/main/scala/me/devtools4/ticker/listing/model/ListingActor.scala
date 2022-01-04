package me.devtools4.ticker.listing.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import co.alphavantage.service.AVantageQueryService
import me.devtools4.ticker.listing.model.ListingActor._
import me.devtools4.ticker.listing.model.TickerActor.{GetOverviewCmd, OverviewPendingEvent}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

case class ListingStatus(symbol: String, name: String, exchange: String, assetType: String, ipoDate: String, delistingDate: String, status: String)

class ListingActor(private val service: AVantageQueryService) extends Actor with ActorLogging {
  type ListingState = (String, Map[String, ActorRef])

  implicit val ec = ExecutionContext.global

  override def receive: Receive = {
    case GetListingCmd =>
      context.become(receiving)
      Future {
        service.activeListing()
      }.onComplete {
        case Success(res) =>
          log.info("Received activeListing response")
          self ! ListingReadyEvent(res)
        case Failure(err) =>
          log.error("activeListing failed, error={}", err.getMessage, err)
          self ! ListingErrorEvent(err)
      }
      sender() ! ListingPendingEvent
  }

  def receiving: Receive = {
    case GetListingCmd => sender() ! ListingPendingEvent
    case GetListingSymbolCmd(_) => sender() ! OverviewPendingEvent
    case ListingReadyEvent(csv) =>
      context.become(received(csv, Map()))
    case ListingErrorEvent(err) =>
      context.become(error(err))
  }

  def received: ListingState => Receive = {
    case (csv, map) => {
      case GetListingCmd =>
        sender() ! ListingReadyEvent(csv)
      case GetListingSymbolCmd(symbol) =>
        val ref = if (map.contains(symbol)) {
          map(symbol)
        } else {
          val a = context.actorOf(TickerActor.props(symbol, service), symbol)
          context.become(received(csv, map + (symbol -> a)))
          a
        }

        ref ! GetOverviewCmd
        sender() ! OverviewPendingEvent
    }
  }

  def error: Throwable => Receive = err => {
    case GetListingCmd => sender() ! ListingErrorEvent(err)
  }
}

object ListingActor {
  def props(service: AVantageQueryService): Props =
    Props(new ListingActor(service))

  sealed trait ListingEvent

  case class GetListingSymbolCmd(symbol: String)

  case class ListingReadyEvent(csv: String) extends ListingEvent

  case class ListingErrorEvent(err: Throwable) extends ListingEvent

  case object GetListingCmd

  case object ListingPendingEvent extends ListingEvent

}
