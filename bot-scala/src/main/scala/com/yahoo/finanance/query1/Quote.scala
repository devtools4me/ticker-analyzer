package com.yahoo.finanance.query1

import io.circe._
import io.circe.generic.semiauto._

import java.util.Calendar

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
                  postMarketChangePercent: Double,
                  postMarketTime: Int,
                  postMarketPrice: Double,
                  postMarketChange: Double,
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
                  financialCurrency: String,
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
                  dividendDate: Calendar,
                  earningsTimestamp: Calendar,
                  earningsTimestampStart: Calendar,
                  earningsTimestampEnd: Calendar,
                  trailingAnnualDividendRate: Double,
                  trailingPE: Double,
                  trailingAnnualDividendYield: Double,
                  epsTrailingTwelveMonths: Double,
                  epsForward: Double,
                  epsCurrentYear: Double,
                  priceEpsCurrentYear: Double,
                  sharesOutstanding: Long,
                  bookValue: Double,
                  fiftyDayAverage: Double,
                  fiftyDayAverageChange: Double,
                  fiftyDayAverageChangePercent: Double,
                  twoHundredDayAverage: Double,
                  twoHundredDayAverageChange: Double,
                  twoHundredDayAverageChangePercent: Double,
                  marketCap: Long,
                  forwardPE: Double,
                  priceToBook: Double,
                  sourceInterval: Int,
                  exchangeDataDelayedBy: Int,
                  averageAnalystRating: String,
                  tradeable: Boolean,
                  shortName: String,
                  exchangeTimezoneShortName: String,
                  gmtOffSetMilliseconds: Int,
                  esgPopulated: Boolean,
                  displayName: String,
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

  private def calendar(timestamp: Long): Calendar = {
    val calendar = Calendar.getInstance
    calendar.setTimeInMillis(timestamp * 1000)
    calendar
  }

  private def timestamp(calendar: Calendar): Long = calendar.getTimeInMillis / 1000
}