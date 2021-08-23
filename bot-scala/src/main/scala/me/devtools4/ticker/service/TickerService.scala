package me.devtools4.ticker.service

import com.yahoo.finanance.query1.{Query1Api, Quote}
import me.devtools4.ticker.api.Period
import me.devtools4.ticker.df.{Etfs, Ohlcv}

import scala.util.{Failure, Success, Try}

class TickerService(client: Query1Api, etfs: Etfs) {
  def quote(sym: String): Option[Quote] = client.quote(sym)
    .toOption
    .map(_.quoteResponse.result)
    .flatMap(_.headOption)
    .map(enrich)

  def history(sym: String, period: Period): Try[Array[Byte]] = {
    val times = period.times match {
      case (s, e) => (Quote.timestamp(s), Quote.timestamp(e))
    }
    for {
      csv <- client.download(sym, period.interval, times._1, times._2)
        .fold(x => Failure(new IllegalArgumentException(x)), Success(_))
      ohlcv <- Ohlcv.of(csv)
      bytes <- ohlcv.png("Adj Close", 500, 500)
    } yield {
      bytes
    }
  }

  def sma(sym: String, period: Period): Try[Array[Byte]] = {
    val times = period.times match {
      case (s, e) => (Quote.timestamp(s), Quote.timestamp(e))
    }
    for {
      csv <- client.download(sym, period.interval, times._1, times._2)
        .fold(x => Failure(new IllegalArgumentException(x)), Success(_))
      ohlcv <- Ohlcv.of(csv)
      bytes <- ohlcv.smaPng("Adj Close", 500, 500)
    } yield {
      bytes
    }
  }

  private def enrich(q: Quote): Quote = etfs.find(q.symbol) match {
    case Some(etf) => q.copy(
      expenseRatio = Option(etf.expenseRatio),
      aum = Option(etf.aum))
    case _ => q
  }
}