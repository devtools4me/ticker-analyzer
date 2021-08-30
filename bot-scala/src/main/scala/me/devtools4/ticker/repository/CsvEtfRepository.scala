package me.devtools4.ticker.repository

import cats.effect.IO
import me.devtools4.ticker.api.Etf
import me.devtools4.ticker.df.{Etfs, etfs}

import scala.util.Try

class CsvEtfRepository(x: Etfs) extends EtfRepository[IO] {
  override def find(sym: String): IO[List[Etf]] = IO {
    import me.devtools4.ticker.df._
    x.find(sym).toList
  }
}

object CsvEtfRepository {
  def apply(resource: String): Try[CsvEtfRepository] =
    etfs(resource)
      .map(new CsvEtfRepository(_))
}