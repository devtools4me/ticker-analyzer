package co.alphavantage.service;

import co.alphavantage.AVantageQueryApi;
import co.alphavantage.AVantageQueryApi.Function;
import co.alphavantage.OverviewResponse;

public class AVantageQueryServiceImpl implements AVantageQueryService {
  private final AVantageQueryApi api;
  private final String token;

  public AVantageQueryServiceImpl(AVantageQueryApi api, String token) {
    this.api = api;
    this.token = token;
  }

  @Override
  public OverviewResponse companyOverview(String symbol) {
    return api.query(Function.OVERVIEW, symbol, token);
  }

  @Override
  public String activeListing() {
    return api.listing(token).body().toString();
  }
}