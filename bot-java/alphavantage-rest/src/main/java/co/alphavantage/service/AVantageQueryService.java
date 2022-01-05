package co.alphavantage.service;

import co.alphavantage.OverviewResponse;

public interface AVantageQueryService {

  OverviewResponse companyOverview(String symbol);

  String activeListing();
}
