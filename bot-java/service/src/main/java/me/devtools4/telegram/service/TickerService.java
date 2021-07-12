package me.devtools4.telegram.service;

import com.yahoo.finanance.query1.Query1Api;
import com.yahoo.finanance.query1.Quote;

public class TickerService {

  private final Query1Api api;

  public TickerService(Query1Api api) {
    this.api = api;
  }

  public Quote quote(String id) {
    return api.quote(id)
        .getQuoteResponse()
        .getResult()
        .stream()
        .findFirst()
        .orElse(null);
  }
}