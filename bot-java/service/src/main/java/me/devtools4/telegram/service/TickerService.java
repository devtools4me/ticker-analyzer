package me.devtools4.telegram.service;

import static com.yahoo.finanance.query1.Query1Api.DAY;
import static com.yahoo.finanance.query1.Query1Api.bodyAsString;
import static com.yahoo.finanance.query1.Query1Api.timestamp;

import com.yahoo.finanance.query1.Query1Api;
import com.yahoo.finanance.query1.Quote;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

  public String history(String id) {
    var res = api.download(id, DAY,
        timestamp(LocalDateTime.now().minus(1, ChronoUnit.MONTHS)),
        timestamp(LocalDateTime.now()));
    return bodyAsString(res);
  }
}