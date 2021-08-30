package me.devtools4.ticker.repository

import me.devtools4.ticker.api.Etf

trait EtfRepository[F[_]] {
  def find(sym: String): F[List[Etf]]
}