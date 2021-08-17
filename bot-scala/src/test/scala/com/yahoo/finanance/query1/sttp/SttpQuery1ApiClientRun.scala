package com.yahoo.finanance.query1.sttp

import com.yahoo.finanance.query1.Quote.timestamp

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object SttpQuery1ApiClientRun extends App {
  var client = SttpQuery1ApiClient("https://query1.finance.yahoo.com")
  client.quote("VIOO")
    .map(x => x.quoteResponse.result) match {
    case Left(err) => println(err)
    case Right(x) => x.foreach(println(_))
  }

  client.download("GDL", "1d",
    timestamp(LocalDateTime.now.minus(1, ChronoUnit.MONTHS)),
    timestamp(LocalDateTime.now)) match {
    case Left(err) => println(err)
    case Right(x) => println(x)
  }
}