package me.devtools4.telegram.service;

import static com.yahoo.finanance.query1.Query1Api.DAY;
import static com.yahoo.finanance.query1.Query1Api.WEEK;
import static com.yahoo.finanance.query1.Query1Api.bodyAsString;
import static me.devtools4.telegram.api.TickerApi.BLSH;
import static me.devtools4.telegram.api.TickerApi.HISTORY;
import static me.devtools4.telegram.api.TickerApi.QUOTE;
import static me.devtools4.telegram.api.TickerApi.SMA;

import com.yahoo.finanance.query1.Query1Api;
import com.yahoo.finanance.query1.Quote;
import com.yahoo.finanance.query1.Quote.QuoteType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import me.devtools4.aops.annotations.Trace;
import me.devtools4.telegram.api.Period;
import me.devtools4.telegram.api.StartInfo;
import me.devtools4.telegram.df.EtfRepository;
import me.devtools4.telegram.df.Ohlcv;
import me.devtools4.telegram.df.PngProps;

public class TickerService {

  private final Query1Api api;
  private final EtfRepository etfRepository;

  public TickerService(Query1Api api, EtfRepository etfRepository) {
    this.api = api;
    this.etfRepository = etfRepository;
  }

  private static String interval(Period period) {
    switch (period) {
      case OneMonth:
      case ThreeMonths:
      case SixMonths:
      case OneYear:
      case FiveYears:
        return DAY;
      case TenYears:
      case TwentyYears:
        return WEEK;
      default:
        throw new IllegalArgumentException("Unsupported " + period);
    }
  }

  public List<StartInfo> start() {
    return List.of(
        StartInfo.of(QUOTE, QUOTE),
        StartInfo.of(HISTORY, HISTORY),
        StartInfo.of(SMA, SMA),
        StartInfo.of(BLSH, BLSH)
    );
  }

  @Trace(level = "INFO")
  public Quote quote(String id) {
    return api.quote(id)
        .getQuoteResponse()
        .getResult()
        .stream()
        .findFirst()
        .map(x -> {
          if (QuoteType.ETF.name().equals(x.getQuoteType())) {
            etfRepository.find(id)
                .ifPresent(etf -> {
                  x.setExpenseRatio(etf.getExpenseRatio());
                  x.setAum(etf.getAum());
                });
          }
          return x;
        })
        .orElse(null);
  }

  @Trace(level = "INFO")
  public byte[] history(String id, Period period) {
    var times = period.times()
        .stream()
        .map(Query1Api::timestamp)
        .collect(Collectors.toList());
    var res = api.download(id, interval(period), times.get(0), times.get(1));
    var csv = bodyAsString(res);
    var ohlcv = new Ohlcv(csv);
    try (var os = new ByteArrayOutputStream()) {
      ohlcv.png(os, PngProps.builder()
          .rowKeyColumnName("Date")
          .columnName("Adj Close")
          .width(500)
          .height(500)
          .build());
      return os.toByteArray();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  @Trace(level = "INFO")
  public byte[] sma(String id, Period period) {
    var times = period.times()
        .stream()
        .map(Query1Api::timestamp)
        .collect(Collectors.toList());
    var res = api.download(id, interval(period), times.get(0), times.get(1));
    var csv = bodyAsString(res);
    var ohlcv = new Ohlcv(csv);
    try (var os = new ByteArrayOutputStream()) {
      ohlcv.smaPng(os, PngProps.builder()
          .rowKeyColumnName("Date")
          .columnName("Adj Close")
          .width(600)
          .height(500)
          .build());
      return os.toByteArray();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  @Trace(level = "INFO")
  public byte[] blsh(String id, Period period) {
    var times = period.times()
        .stream()
        .map(Query1Api::timestamp)
        .collect(Collectors.toList());
    var res = api.download(id, interval(period), times.get(0), times.get(1));
    var csv = bodyAsString(res);
    var ohlcv = new Ohlcv(csv);
    try (var os = new ByteArrayOutputStream()) {
      ohlcv.blshPng(os, PngProps.builder()
          .rowKeyColumnName("Date")
          .columnName("Adj Close")
          .width(600)
          .height(500)
          .build());
      return os.toByteArray();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}