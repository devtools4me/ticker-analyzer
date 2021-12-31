package me.devtools4.ticker.listing

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import co.alphavantage.service.AVantageQueryService
import co.alphavantage.{AVantageQueryApi, AlphaVantageExceptionDecoder}
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Logger.{ErrorLogger, Level}
import feign.codec.{JacksonDecoder, JacksonEncoder}
import feign.{Feign, Request}
import me.devtools4.ticker.listing.TickerListingApp.{actor, overview, timeout}
import me.devtools4.ticker.listing.effect.IO
import me.devtools4.ticker.listing.model.ListingActor.{GetListingCmd, GetListingSymbolCmd, ListingEvent}
import me.devtools4.ticker.listing.model.{ListingActor, TickerActor}
import me.devtools4.ticker.listing.model.TickerActor.{GetOverviewCmd, OverviewEvent}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Promise}
import scala.util.{Failure, Success}

object ListingApp extends App {
  val system = ActorSystem("Listing")
  val console = Console.system
  val maxAttempt = 3
  val avService = queryService(args(0), args(1))
  implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)
  implicit val ec: ExecutionContext = ExecutionContext.global

  import console._

  val program = overviewThenRepeat
    .andThen(IO.exit())

  program.unsafeRun()

  private def overviewThenRepeat(implicit ec: ExecutionContext): IO[Boolean] = {
    listing.flatMap { _ =>
      repeat.retry(maxAttempt)
    }.flatMap { x =>
      if (x) overviewThenRepeat
      else IO(x)
    }
  }

  private def listing(implicit ec: ExecutionContext): IO[String] = for {
    ref <- actor
    _ <- listing(ref)
    _ <- writeLine("Ticker name?")
    symbol <- readLine
    ref <- actor
    res <- overview(ref, symbol)
    _ <- writeLine(s"Event=$res")
  } yield {
    symbol
  }

  private def listing(ref: ActorRef)(implicit ec: ExecutionContext): IO[ListingEvent] = IO.dispatch {
    val f = ref ? GetListingCmd
    Await.result(f, timeout.duration).asInstanceOf[ListingEvent]
  }(ec)

  private def overview(ref: ActorRef, symbol: String)(implicit ec: ExecutionContext): IO[ListingEvent] = IO.dispatch {
    val f = ref ? GetListingSymbolCmd(symbol)
    Await.result(f, timeout.duration).asInstanceOf[ListingEvent]
  }(ec)

  private def actor(implicit ec: ExecutionContext): IO[ActorRef] = IO.dispatch {
    val promise = Promise[ActorRef]()
    system.actorSelection("/user/listing")
      .resolveOne()
      .onComplete {
        case Success(actor) => promise.success(actor)
        case Failure(_) => promise.success(system.actorOf(ListingActor.props(avService), "listing"))
      }
    Await.result(promise.future, timeout.duration)
  }(ec)

  private def repeat = for {
    _ <- writeLine("Repeat? [y/n]")
    s <- readLine
    yn <- parseLineToBoolean(s).onError { _ =>
      writeLine("""Incorrect format, enter "Y" for Yes or "N" for "No"""")
    }
  } yield {
    yn
  }

  private def parseLineToBoolean(line: String) = IO {
    line.toLowerCase match {
      case "y" => true
      case "n" => false
      case _ => throw new IllegalArgumentException("Invalid input, expected y/n")
    }
  }

  private def queryService(url: String, token: String) = {
    val mapper: ObjectMapper = new ObjectMapper()
    val decoder = new JacksonDecoder(mapper)
    val encoder = new JacksonEncoder(mapper)
    val api = Feign.builder
      .encoder(encoder)
      .decoder(decoder)
      .errorDecoder(new AlphaVantageExceptionDecoder(decoder))
      .logger(new ErrorLogger())
      .logLevel(Level.NONE)
      .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
      .target(classOf[AVantageQueryApi], url)
    new AVantageQueryService(api, token)
  }
}