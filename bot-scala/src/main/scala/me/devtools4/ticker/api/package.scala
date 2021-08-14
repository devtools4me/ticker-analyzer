package me.devtools4.ticker

package object api {
  val QUOTE = "/quote"
  val HISTORY = "/history"
  val SMA = "/sma"

  sealed trait Period

  case object UnknownPeriod extends Period
  case object OneMonth extends Period
  case object ThreeMonths extends Period
  case object SixMonths extends Period
  case object OneYear extends Period
  case object FiveYears extends Period
  case object TenYears extends Period
  case object TwentyYears extends Period
  case object Max extends Period

  object Period {
    def apply(s: String): Period = s match {
      case "1m" => OneMonth
      case "3m" => ThreeMonths
      case "6m" => SixMonths
      case "1y" => OneYear
      case "5y" => FiveYears
      case "10y" => TenYears
      case "20y" => TwentyYears
      case "max" => Max
      case _     => UnknownPeriod
    }
  }

  sealed trait TickerCmd {}

  case class UnknownCmd(s: String) extends TickerCmd
  case object Start extends TickerCmd
  case class Quote(symbol: String) extends TickerCmd
  case class History(symbol: String, period: Period) extends TickerCmd
  case class Sma(symbol: String, period: Period) extends TickerCmd

  object TickerCmd {
    def apply(s: String): TickerCmd = s.split("/").toList match {
      case List("", "start") => Start
      case List("", "quote", sym) => Quote(sym)
      case List("", "history", sym) => History(sym, OneMonth)
      case List("", "history", p, sym) => Period(p) match {
        case UnknownPeriod => UnknownCmd(s)
        case period => History(sym, period)
      }
      case List("", "sma", sym) => Sma(sym, OneMonth)
      case List("", "sma", p, sym) => Period(p) match {
        case UnknownPeriod => UnknownCmd(s)
        case period => Sma(sym, period)
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