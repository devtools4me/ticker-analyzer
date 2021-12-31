package me.devtools4.ticker.listing.model

import akka.actor.{Actor, Props}
import co.alphavantage.service.AVantageQueryService
import me.devtools4.ticker.listing.model.ListingActor._
import me.devtools4.ticker.listing.model.TickerActor.OverviewErrorEvent

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

case class ListingStatus(symbol: String, name: String, exchange: String, assetType: String, ipoDate: String, delistingDate: String, status: String)

class ListingActor(private val service: AVantageQueryService) extends Actor {
  override def receive: Receive = idle

  implicit val ec = ExecutionContext.global

  def idle: Receive = {
    case GetListingCmd =>
      context.become(receiving)
      Future {
        service.activeListing()
      }.onComplete {
        case Success(res) => self ! ListingReadyEvent(res)
        case Failure(err) => self ! ListingErrorEvent(err)
      }
      sender() ! ListingPendingEvent
  }

  def receiving: Receive = {
    case GetListingCmd => sender() ! ListingPendingEvent
    case ListingReadyEvent(csv) =>
      context.become(received(csv))
    case OverviewErrorEvent(err) =>
      context.become(error(err))
  }

  def received: String => Receive = csv => {
    case GetListingCmd => sender() ! ListingReadyEvent(csv)
    case GetListingSymbolCmd(symbol) => sender() ! ListingReadyEvent(csv)
  }

  def error: Throwable => Receive = err => {
    case GetListingCmd => sender() ! ListingErrorEvent(err)
  }
}

object ListingActor {
  def props(service: AVantageQueryService): Props =
    Props(new ListingActor(service))

  case object GetListingCmd

  case class GetListingSymbolCmd(symbol: String)

  sealed trait ListingEvent

  case object ListingPendingEvent extends ListingEvent

  case class ListingReadyEvent(csv: String) extends ListingEvent

  case class ListingErrorEvent(err: Throwable) extends ListingEvent

}
