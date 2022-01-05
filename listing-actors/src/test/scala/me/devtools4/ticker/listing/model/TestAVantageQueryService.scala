package me.devtools4.ticker.listing.model

import co.alphavantage.OverviewResponse
import co.alphavantage.service.AVantageQueryService

class TestAVantageQueryService extends AVantageQueryService {
  override def companyOverview(symbol: String): OverviewResponse = ???

  override def activeListing(): String = ???
}

object TestAVantageQueryService {
  def apply(): AVantageQueryService = new TestAVantageQueryService()
}