package me.devtools4.ticker.listing

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import co.alphavantage.service.{AVantageQueryService, AVantageQueryServiceImpl}
import co.alphavantage.{AVantageQueryApi, AlphaVantageExceptionDecoder}
import com.fasterxml.jackson.databind.ObjectMapper
import feign.Logger.{ErrorLogger, Level}
import feign.codec.{JacksonDecoder, JacksonEncoder}
import feign.{Feign, Request}
import me.devtools4.ticker.listing.effect.IO
import me.devtools4.ticker.listing.model.ListingActor
import me.devtools4.ticker.listing.model.ListingActor.{GetListingCmd, GetListingSymbolCmd, ListingEvent}

import java.util.concurrent.TimeUnit
import scala.concurrent.{Await, ExecutionContext}

object ListingApp extends App {
  val console = Console.system
  val maxAttempt = 3
  val avService = queryService(args(0), args(1))
  implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)
  implicit val ec: ExecutionContext = ExecutionContext.global

  import console._

  val program = init
    .flatMap(overviewThenRepeat(_))
    .andThen(IO.exit())

  program.unsafeRun()

  private def init(implicit ec: ExecutionContext) = for {
    system <- IO.dispatch(ActorSystem("Listing"))(ec)
    ref <- IO.dispatch(system.actorOf(ListingActor.props(avService), "listing"))(ec)
    res <- listing(ref)
    _ <- writeLine(s"Response=$res")
  } yield {
    ref
  }

  private def overviewThenRepeat(ref: ActorRef)(implicit ec: ExecutionContext): IO[Boolean] = {
    listing2(ref).flatMap { _ =>
      repeat.retry(maxAttempt)
    }.flatMap { x =>
      if (x) overviewThenRepeat(ref)
      else IO(x)
    }
  }

  private def listing2(ref: ActorRef)(implicit ec: ExecutionContext): IO[String] = for {
    _ <- writeLine("Ticker name?")
    symbol <- readLine
    res <- overview(ref, symbol)
    _ <- writeLine(s"Event=$res")
  } yield {
    symbol
  }

  private def listing(ref: ActorRef)(implicit ec: ExecutionContext): IO[ListingEvent] = IO.dispatch {
    val f = ref ? GetListingCmd
    Await.result(f, timeout.duration).asInstanceOf[ListingEvent]
  }(ec)

  private def overview(ref: ActorRef, symbol: String)(implicit ec: ExecutionContext): IO[AnyRef] = IO.dispatch {
    Await.result(ref ? GetListingSymbolCmd(symbol), timeout.duration).asInstanceOf[AnyRef]
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
    new AVantageQueryServiceImpl(api, token)
  }
}