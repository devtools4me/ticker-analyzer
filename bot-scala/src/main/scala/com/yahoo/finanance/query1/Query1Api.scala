package com.yahoo.finanance.query1

trait Query1Api {
  def quote(symbol_id: String): Either[String, QuoteResponseResponse]

  def download(symbol_id: String, interval: String, from_date: Long, to_date: Long): String
}