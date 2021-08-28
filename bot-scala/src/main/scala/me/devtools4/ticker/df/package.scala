package me.devtools4.ticker

import java.time.LocalDate

package object df {

  case class Etf(symbol: String,
                 startDate: LocalDate,
                 name: String,
                 segment: String,
                 issuer: String,
                 expenseRatio: String,
                 aum: String)

}