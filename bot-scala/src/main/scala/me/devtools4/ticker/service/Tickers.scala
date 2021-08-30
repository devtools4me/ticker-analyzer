package me.devtools4.ticker.service

import com.yahoo.finanance.query1.Quote
import me.devtools4.ticker.api.Period

trait Tickers[F[_]] {
  def quote(sym: String): F[Quote]

  def history(sym: String, period: Period): F[Array[Byte]]

  def sma(sym: String, period: Period): F[Array[Byte]]
}

import cats.effect._
import cats.implicits._
import com.yahoo.finanance.query1.{Query1ApiP, Quote}
import me.devtools4.ticker.api.Period
import me.devtools4.ticker.df._
import me.devtools4.ticker.repository.EtfRepository

class IoTickers(client: Query1ApiP[IO], repo: EtfRepository[IO]) extends Tickers[IO] {
  override def quote(sym: String): IO[Quote] = {
    val a = client.quote(sym)
      .map(x => x.quoteResponse.result)
    val b = repo.find(sym)
    (a, b).parMapN { (x, y) =>
      (x, y) match {
        case (q :: _, e :: _) =>
          q.copyWith(e.expenseRatio, e.aum)
        case (q :: _, _) =>
          q
        case _ => throw new RuntimeException()
      }
    }
  }

  override def history(sym: String, period: Period): IO[Array[Byte]] = period.times match {
    case (s, e) =>
      client.download(sym, period.interval, Quote.timestamp(s), Quote.timestamp(e))
        .flatMap(x => IO.fromTry(df(x)))
        .flatMap(x => IO.fromTry(x.png("Adj Close", 500, 500)))
  }

  override def sma(sym: String, period: Period): IO[Array[Byte]] = period.times match {
    case (s, e) =>
      client.download(sym, period.interval, Quote.timestamp(s), Quote.timestamp(e))
        .flatMap(x => IO.fromTry(df(x)))
        .flatMap(x => IO.fromTry(x.smaPng("Adj Close", 500, 500)))
  }
}

object IoTickers {
  def apply(client: Query1ApiP[IO], repo: EtfRepository[IO]): IoTickers =
    new IoTickers(client, repo)
}