package com.yahoo.finanance.query1;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;
import com.dslplatform.json.TimestampConverter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.function.Function;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@CompiledJson
public class Quote {
  public enum QuoteType {
    ETF,
    EQUITY
  }

  private String language;
  private String region;
  private String quoteType;
  private String quoteSourceName;
  private boolean triggerable;
  private String currency;
  private String marketState;
  private String exchange;
  private String longName;
  private String messageBoardId;
  private String exchangeTimezoneName;
  private String market;
  private long firstTradeDateMilliseconds;
  private int priceHint;
  private double postMarketChangePercent;
  private int postMarketTime;
  private double postMarketPrice;
  private double postMarketChange;
  private double regularMarketChange;
  private double regularMarketChangePercent;
  private int regularMarketTime;
  private double regularMarketPrice;
  private double regularMarketDayHigh;
  private String regularMarketDayRange;
  private double regularMarketDayLow;
  private int regularMarketVolume;
  private double regularMarketPreviousClose;
  private double bid;
  private double ask;
  private int bidSize;
  private int askSize;
  private String fullExchangeName;
  private String financialCurrency;
  private double regularMarketOpen;
  private int averageDailyVolume3Month;
  private int averageDailyVolume10Day;
  private double fiftyTwoWeekLowChange;
  private double fiftyTwoWeekLowChangePercent;
  private String fiftyTwoWeekRange;
  private double fiftyTwoWeekHighChange;
  private double fiftyTwoWeekHighChangePercent;
  private double fiftyTwoWeekLow;
  private double fiftyTwoWeekHigh;

  @JsonAttribute(converter = TimestampConverter.class)
  private Calendar dividendDate;

  @JsonAttribute(converter = TimestampConverter.class)
  private Calendar earningsTimestamp;

  @JsonAttribute(converter = TimestampConverter.class)
  private Calendar earningsTimestampStart;

  @JsonAttribute(converter = TimestampConverter.class)
  private Calendar earningsTimestampEnd;

  private double trailingAnnualDividendRate;
  private double trailingPE;
  private double trailingAnnualDividendYield;
  private double epsTrailingTwelveMonths;
  private double epsForward;
  private double epsCurrentYear;
  private double priceEpsCurrentYear;
  private long sharesOutstanding;
  private double bookValue;
  private double fiftyDayAverage;
  private double fiftyDayAverageChange;
  private double fiftyDayAverageChangePercent;
  private double twoHundredDayAverage;
  private double twoHundredDayAverageChange;
  private double twoHundredDayAverageChangePercent;
  private long marketCap;
  private double forwardPE;
  private double priceToBook;
  private int sourceInterval;
  private int exchangeDataDelayedBy;
  private String averageAnalystRating;
  private boolean tradeable;
  private String shortName;
  private String exchangeTimezoneShortName;
  private int gmtOffSetMilliseconds;
  private boolean esgPopulated;
  private String displayName;
  private String symbol;

  private String expenseRatio;
  private String aum;

  public static Function<Calendar, String> calendar2str() {
    return time -> Optional.ofNullable(time)
        .map(x -> x.getTime().toString())
        .orElse("");
  }

  public Function<Object, Object> dividendDateStr() {
    return x -> calendar2str().apply(dividendDate);
  }

  public Function<Object, Object> earningsTimestampStr() {
    return x -> calendar2str().apply(earningsTimestamp);
  }

  public Function<Object, Object> trailingAnnualDividendYieldStr() {
    return x -> new DecimalFormat("#0.00%").format(trailingAnnualDividendYield);
  }
}