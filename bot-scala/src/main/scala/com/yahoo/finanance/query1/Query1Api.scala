package com.yahoo.finanance.query1

trait Query1Api {
  def quote(symbol_id: String): Either[String, QuoteResponseResponse]

  def download(symbol_id: String, interval: String, from_date: Long, to_date: Long): Either[String, String]
}

trait Query1ApiP[F[_]] {
  def quote(symbol_id: String): F[QuoteResponseResponse]

  def download(symbol_id: String, interval: String, from_date: Long, to_date: Long): F[String]
}