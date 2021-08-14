package me.devtools4.ticker

package object api {
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

  sealed trait Command {}

  case class UnknownCommand(s: String) extends Command
  case object Start extends Command
  case class Quote(symbol: String) extends Command
  case class History(symbol: String, period: Period) extends Command
  case class Sma(symbol: String, period: Period) extends Command

  object Command {
    def apply(s: String): Command = s.split("/").toList match {
      case List("", "start") => Start
      case List("", "quote", sym) => Quote(sym)
      case List("", "history", sym) => History(sym, OneMonth)
      case List("", "history", p, sym) => Period(p) match {
        case UnknownPeriod => UnknownCommand(s)
        case period => History(sym, period)
      }
      case List("", "sma", sym) => Sma(sym, OneMonth)
      case List("", "sma", p, sym) => Period(p) match {
        case UnknownPeriod => UnknownCommand(s)
        case period => Sma(sym, period)
      }
      case list => UnknownCommand(s"[$s] => $list")
    }
  }
}