package com.yahoo.finanance.query1.sttp

object SttpQuery1ApiClientRun extends App {
  var client = SttpQuery1ApiClient("https://query1.finance.yahoo.com")
  client.quote("VIOO")
    .map(x => x.quoteResponse.result) match {
    case Left(err) => println(err)
    case Right(x) => x.foreach(println(_))
  }
}