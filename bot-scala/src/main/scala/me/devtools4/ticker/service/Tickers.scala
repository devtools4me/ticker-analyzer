package me.devtools4.ticker.service

import com.yahoo.finanance.query1.Quote
import me.devtools4.ticker.api.Period
import me.devtools4.ticker.ops._

trait Tickers[F[_]] {
  def quote(sym: String): F[Either[String, Quote]]

  def history(sym: String, period: Period): F[Either[String, Array[Byte]]]

  def sma(sym: String, period: Period): F[Either[String, Array[Byte]]]
}

import cats.effect._
import cats.implicits._
import com.yahoo.finanance.query1.{Query1ApiP, Quote}
import me.devtools4.ticker.api.Period
import me.devtools4.ticker.df._
import me.devtools4.ticker.repository.EtfRepository

class IoTickers(client: Query1ApiP[IO], repo: EtfRepository[IO]) extends Tickers[IO] {
  override def quote(sym: String): IO[Either[String, Quote]] = {
    val a = client.quote(sym)
      .map(x => x.fold(Left(_), q => Right(q.quoteResponse.result)))
    val b = repo.find(sym)
    (a, b).parMapN { (x, y) =>
      (x, y) match {
        case (Right(q :: _), e :: _) =>
          Right(q.copyWith(e.expenseRatio, e.aum))
        case (Right(q :: _), _) =>
          Right(q)
        case _ => Left("???")
      }
    }
  }

  override def history(sym: String, period: Period): IO[Either[String, Array[Byte]]] = period.times match {
    case (s, e) =>
      client.download(sym, period.interval, Quote.timestamp(s), Quote.timestamp(e))
        .flatMap(x => IO.fromTry(x._toTry))
        .flatMap(x => IO.fromTry(df(x)))
        .flatMap(x => IO(x.png("Adj Close", 500, 500)._toEither))
  }

  override def sma(sym: String, period: Period): IO[Either[String, Array[Byte]]] = period.times match {
    case (s, e) =>
      client.download(sym, period.interval, Quote.timestamp(s), Quote.timestamp(e))
        .flatMap(x => IO.fromTry(x._toTry))
        .flatMap(x => IO.fromTry(df(x)))
        .flatMap(x => IO(x.smaPng("Adj Close", 500, 500)._toEither))
  }
}

object IoTickers {
  def apply(client: Query1ApiP[IO], repo: EtfRepository[IO]): IoTickers =
    new IoTickers(client, repo)
}