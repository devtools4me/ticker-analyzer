package me.devtools4.telegram.service;

import static com.yahoo.finanance.query1.Query1Api.DAY;
import static com.yahoo.finanance.query1.Query1Api.bodyAsString;
import static com.yahoo.finanance.query1.Query1Api.timestamp;

import com.yahoo.finanance.query1.Query1Api;
import com.yahoo.finanance.query1.Quote;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import me.devtools4.telegram.df.Ohlcv;

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

  public byte[] history(String id) {
    var res = api.download(id, DAY,
        timestamp(LocalDateTime.now().minus(1, ChronoUnit.MONTHS)),
        timestamp(LocalDateTime.now()));
    var csv = bodyAsString(res);
    var ohlcv = new Ohlcv(csv);
    try (var os = new ByteArrayOutputStream()) {
      ohlcv.png(os, 500, 500);
      return os.toByteArray();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}