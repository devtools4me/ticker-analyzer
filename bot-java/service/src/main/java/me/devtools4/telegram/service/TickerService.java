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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import me.devtools4.aops.annotations.Trace;
import me.devtools4.telegram.api.Command;
import me.devtools4.telegram.api.Period;
import me.devtools4.telegram.api.StartInfo;
import me.devtools4.telegram.df.EtfRepository;
import me.devtools4.telegram.df.PngProps;
import me.devtools4.telegram.df.chart.ChartStrategy;

public class TickerService {

  private final Query1Api api;
  private final EtfRepository etfRepository;
  private final Map<Command, ChartStrategy> strategies;

  public TickerService(Query1Api api, EtfRepository etfRepository, Map<Command, ChartStrategy> strategies) {
    this.api = api;
    this.etfRepository = etfRepository;
    this.strategies = strategies;
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
  public byte[] png(Command cmd, String id, Period period) {
    var times = period.times()
        .stream()
        .map(Query1Api::timestamp)
        .collect(Collectors.toList());
    var res = api.download(id, interval(period), times.get(0), times.get(1));
    var csv = bodyAsString(res);
    return Optional.ofNullable(strategies.get(cmd))
        .orElseThrow(() -> new IllegalArgumentException("No strategy, cmd=" + cmd))
        .png(csv, pngProps(cmd));
  }

  private static PngProps pngProps(Command cmd) {
    return PngProps.builder()
        .rowKeyColumnName("Date")
        .columnName("Adj Close")
        .width(cmd.is(Command.HISTORY) ? 500 : 600)
        .height(500)
        .build();
  }
}