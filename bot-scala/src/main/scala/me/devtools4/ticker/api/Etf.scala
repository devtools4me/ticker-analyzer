package me.devtools4.ticker.api

import java.time.LocalDate

case class Etf(symbol: String,
               startDate: LocalDate,
               name: String,
               segment: String,
               issuer: String,
               expenseRatio: String,
               aum: String)
