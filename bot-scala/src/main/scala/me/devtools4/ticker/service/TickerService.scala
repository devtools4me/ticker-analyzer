package me.devtools4.ticker.service

import com.yahoo.finanance.query1.{Query1Api, Quote}
import me.devtools4.ticker.api.Period
import me.devtools4.ticker.df.Etfs

import scala.util.{Failure, Success, Try}

class TickerService(client: Query1Api, etfs: Etfs) {
  def quote(sym: String): Option[Quote] = client.quote(sym)
    .toOption
    .map(_.quoteResponse.result)
    .flatMap(_.headOption)
    .map(enrich)

  import me.devtools4.ticker.df._

  def history(sym: String, period: Period): Try[Array[Byte]] = {
    val times = period.times match {
      case (s, e) => (Quote.timestamp(s), Quote.timestamp(e))
    }
    for {
      csv <- client.download(sym, period.interval, times._1, times._2)
        .fold(x => Failure(new IllegalArgumentException(x)), Success(_))
      ohlcv <- df(csv)
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
      ohlcv <- df(csv)
      bytes <- ohlcv.smaPng("Adj Close", 500, 500)
    } yield {
      bytes
    }
  }

  private def enrich(q: Quote): Quote = {
    import Quote._
    etfs.find(q.symbol) match {
      case Some(etf) => q.copyWith(etf.expenseRatio, etf.aum)
      case _ => q
    }
  }
}