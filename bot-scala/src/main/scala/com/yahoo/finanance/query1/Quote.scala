package com.yahoo.finanance.query1

import com.github.mustachejava.{DefaultMustacheFactory, Mustache}
import io.circe._
import io.circe.generic.semiauto._

import java.io.StringWriter
import java.time.{LocalDateTime, ZoneId}
import java.util.{Calendar, Date}
import scala.util.{Try, Using}

case class Quote(
                  language: String,
                  region: String,
                  quoteType: String,
                  quoteSourceName: String,
                  triggerable: Boolean,
                  currency: String,
                  marketState: String,
                  exchange: String,
                  longName: String,
                  messageBoardId: String,
                  exchangeTimezoneName: String,
                  market: String,
                  firstTradeDateMilliseconds: Long,
                  priceHint: Int,
                  postMarketChangePercent: Option[Double],
                  postMarketTime: Option[Int],
                  postMarketPrice: Option[Double],
                  postMarketChange: Option[Double],
                  regularMarketChange: Double,
                  regularMarketChangePercent: Double,
                  regularMarketTime: Int,
                  regularMarketPrice: Double,
                  regularMarketDayHigh: Double,
                  regularMarketDayRange: String,
                  regularMarketDayLow: Double,
                  regularMarketVolume: Int,
                  regularMarketPreviousClose: Double,
                  bid: Double,
                  ask: Double,
                  bidSize: Int,
                  askSize: Int,
                  fullExchangeName: String,
                  financialCurrency: Option[String],
                  regularMarketOpen: Double,
                  averageDailyVolume3Month: Int,
                  averageDailyVolume10Day: Int,
                  fiftyTwoWeekLowChange: Double,
                  fiftyTwoWeekLowChangePercent: Double,
                  fiftyTwoWeekRange: String,
                  fiftyTwoWeekHighChange: Double,
                  fiftyTwoWeekHighChangePercent: Double,
                  fiftyTwoWeekLow: Double,
                  fiftyTwoWeekHigh: Double,
                  dividendDate: Option[Calendar],
                  earningsTimestamp: Option[Calendar],
                  earningsTimestampStart: Option[Calendar],
                  earningsTimestampEnd: Option[Calendar],
                  trailingAnnualDividendRate: Option[Double],
                  trailingPE: Option[Double],
                  trailingAnnualDividendYield: Option[Double],
                  epsTrailingTwelveMonths: Option[Double],
                  epsForward: Option[Double],
                  epsCurrentYear: Option[Double],
                  priceEpsCurrentYear: Option[Double],
                  sharesOutstanding: Option[Long],
                  bookValue: Option[Double],
                  fiftyDayAverage: Double,
                  fiftyDayAverageChange: Double,
                  fiftyDayAverageChangePercent: Double,
                  twoHundredDayAverage: Double,
                  twoHundredDayAverageChange: Double,
                  twoHundredDayAverageChangePercent: Double,
                  marketCap: Option[Long],
                  forwardPE: Option[Double],
                  priceToBook: Option[Double],
                  sourceInterval: Int,
                  exchangeDataDelayedBy: Int,
                  averageAnalystRating: Option[String],
                  tradeable: Boolean,
                  shortName: String,
                  exchangeTimezoneShortName: String,
                  gmtOffSetMilliseconds: Int,
                  esgPopulated: Boolean,
                  displayName: Option[String],
                  symbol: String,
                  expenseRatio: Option[String],
                  aum: Option[String]
                )

object Quote {
  implicit val encoder: Encoder[Quote] = deriveEncoder[Quote]
  implicit val decoder: Decoder[Quote] = deriveDecoder[Quote]

  implicit val calendarEncoder: Encoder[Calendar] =
    (date: Calendar) => Json.fromLong(timestamp(date))

  implicit val calendarDecoder: Decoder[Calendar] =
    (c: HCursor) => c.as[Long].map(x => calendar(x))

  def calendar(timestamp: Long): Calendar = {
    val calendar = Calendar.getInstance
    calendar.setTimeInMillis(timestamp * 1000)
    calendar
  }

  def timestamp(calendar: Calendar): Long = calendar.getTimeInMillis / 1000

  def timestamp(time: LocalDateTime): Long = Date.from(time.atZone(ZoneId.systemDefault).toInstant).getTime / 1000

  lazy val mf = new DefaultMustacheFactory
  lazy val quoteMustache: Mustache = mf.compile("quote.mustache")
  lazy val errorMustache: Mustache = mf.compile("error.mustache")

  implicit class QuoteHelper(q: Quote) {
    def copyWith(expenseRatio: String, aum: String): Quote = q.copy(
      expenseRatio = Option(expenseRatio),
      aum = Option(aum))

    def html: Try[String] = Using(new StringWriter())(w => {
      quoteMustache.execute(w, q).flush()
      w.toString
    })
  }

  implicit class ThrowableHelper(err: Throwable) {
    def html: Try[String] = Using(new StringWriter())(w => {
      errorMustache.execute(w, err).flush()
      w.toString
    })
  }

}