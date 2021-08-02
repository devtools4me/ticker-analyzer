package me.devtools4.telegram.service;

import static com.yahoo.finanance.query1.Query1Api.DAY;
import static com.yahoo.finanance.query1.Query1Api.MONTH;
import static com.yahoo.finanance.query1.Query1Api.WEEK;
import static com.yahoo.finanance.query1.Query1Api.bodyAsString;

import com.yahoo.finanance.query1.Query1Api;
import com.yahoo.finanance.query1.Quote;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.devtools4.telegram.api.Period;
import me.devtools4.telegram.df.Ohlcv;

@Slf4j
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

  public byte[] history(String id, Period period) {
    log.info("id={}, period={}", id, period);

    var times = period.times()
        .stream()
        .map(Query1Api::timestamp)
        .collect(Collectors.toList());
    var res = api.download(id, interval(period), times.get(0), times.get(1));
    var csv = bodyAsString(res);
    var ohlcv = new Ohlcv(csv);
    try (var os = new ByteArrayOutputStream()) {
      ohlcv.png(os, 500, 500);
      return os.toByteArray();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public byte[] sma(String id, Period period) {
    log.info("id={}, period={}", id, period);

    var times = period.times()
        .stream()
        .map(Query1Api::timestamp)
        .collect(Collectors.toList());
    var res = api.download(id, interval(period), times.get(0), times.get(1));
    var csv = bodyAsString(res);
    var ohlcv = new Ohlcv(csv);
    try (var os = new ByteArrayOutputStream()) {
      ohlcv.smaPng(os, 500, 500);
      return os.toByteArray();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  private static String interval(Period period) {
    switch (period) {
      case OneMonth:
        return DAY;
      case ThreeMonths:
      case SixMonths:
        return WEEK;
      case OneYear:
      case FiveYears:
      case TenYears:
      case TwentyYears:
        return MONTH;
      default:
        throw new IllegalArgumentException("Unsupported " + period);
    }
  }
}