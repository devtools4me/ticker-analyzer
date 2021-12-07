package co.alphavantage.service;

import co.alphavantage.AVantageQueryApi;
import co.alphavantage.AVantageQueryApi.Function;
import co.alphavantage.OverviewResponse;

public class AVantageQueryService {
  private final AVantageQueryApi api;
  private final String token;

  public AVantageQueryService(AVantageQueryApi api, String token) {
    this.api = api;
    this.token = token;
  }

  public OverviewResponse companyOverview(String symbol) {
    return api.query(Function.OVERVIEW, symbol, token);
  }
}