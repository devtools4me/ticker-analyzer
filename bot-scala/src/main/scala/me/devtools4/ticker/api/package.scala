package me.devtools4.ticker

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

package object api {
  val QUOTE = "/quote"
  val HISTORY = "/history"
  val SMA = "/sma"

  sealed trait Period {
    def interval: String = throw new UnsupportedOperationException(s"Unsupported interval=$this")
    def times: (LocalDateTime, LocalDateTime) = throw new UnsupportedOperationException(s"Unsupported period=$this")
  }

  case object UnknownPeriod extends Period
  case object OneMonth extends Period {
    override def interval: String = "1d"
    override def times: (LocalDateTime, LocalDateTime) = {
      val now = LocalDateTime.now()
      (now.minus(1, ChronoUnit.MONTHS), now)
    }
  }
  case object ThreeMonths extends Period {
    override def interval: String = "1d"
    override def times: (LocalDateTime, LocalDateTime) = {
      val now = LocalDateTime.now()
      (now.minus(3, ChronoUnit.MONTHS), now)
    }
  }
  case object SixMonths extends Period {
    override def interval: String = "1d"
    override def times: (LocalDateTime, LocalDateTime) = {
      val now = LocalDateTime.now()
      (now.minus(6, ChronoUnit.MONTHS), now)
    }
  }
  case object OneYear extends Period {
    override def interval: String = "1d"
    override def times: (LocalDateTime, LocalDateTime) = {
      val now = LocalDateTime.now()
      (now.minus(1, ChronoUnit.YEARS), now)
    }
  }
  case object FiveYears extends Period {
    override def interval: String = "1d"
    override def times: (LocalDateTime, LocalDateTime) = {
      val now = LocalDateTime.now()
      (now.minus(5, ChronoUnit.YEARS), now)
    }
  }
  case object TenYears extends Period {
    override def interval: String = "5d"
    override def times: (LocalDateTime, LocalDateTime) = {
      val now = LocalDateTime.now()
      (now.minus(10, ChronoUnit.YEARS), now)
    }
  }
  case object TwentyYears extends Period {
    override def interval: String = "5d"
    override def times: (LocalDateTime, LocalDateTime) = {
      val now = LocalDateTime.now()
      (now.minus(20, ChronoUnit.YEARS), now)
    }
  }
  case object Max extends Period

  object Period {
    def apply(s: String): Either[String, Period] = s match {
      case "1m" => Right(OneMonth)
      case "3m" => Right(ThreeMonths)
      case "6m" => Right(SixMonths)
      case "1y" => Right(OneYear)
      case "5y" => Right(FiveYears)
      case "10y" => Right(TenYears)
      case "20y" => Right(TwentyYears)
      case "max" => Right(Max)
      case _     => Left(s"UnknownPeriod $s")
    }
  }

  sealed trait TickerCmd {}

  case class UnknownCmd(s: String) extends TickerCmd
  case object StartCmd extends TickerCmd
  case class QuoteCmd(symbol: String) extends TickerCmd
  case class HistoryCmd(symbol: String, period: Period) extends TickerCmd
  case class SmaCmd(symbol: String, period: Period) extends TickerCmd

  object TickerCmd {
    def apply(s: String): TickerCmd = s.split("/").toList match {
      case List("", "start") => StartCmd
      case List("", "quote", sym) => QuoteCmd(sym)
      case List("", "history", sym) => HistoryCmd(sym, OneMonth)
      case List("", "history", p, sym) => Period(p) match {
        case Left(_) => UnknownCmd(s)
        case Right(period) => HistoryCmd(sym, period)
      }
      case List("", "sma", sym) => SmaCmd(sym, OneMonth)
      case List("", "sma", p, sym) => Period(p) match {
        case Left(_) => UnknownCmd(s)
        case Right(period) => SmaCmd(sym, period)
      }
      case list => UnknownCmd(s"[$s] => $list")
    }
  }

  sealed trait TickerQuery
  case class UnknownQuery(s: String) extends TickerQuery
  case object QuoteQuery extends TickerQuery
  case object HistoryQuery extends TickerQuery
  case object SmaQuery extends TickerQuery

  object TickerQuery {
    def apply(s: String): TickerQuery = s match {
      case "/quote" => QuoteQuery
      case "/history" => HistoryQuery
      case "/sma" => SmaQuery
      case x => UnknownQuery(x)
    }
  }
}