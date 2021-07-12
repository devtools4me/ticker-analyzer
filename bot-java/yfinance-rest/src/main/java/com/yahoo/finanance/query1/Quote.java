package com.yahoo.finanance.query1;

import com.dslplatform.json.CompiledJson;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@CompiledJson
public class Quote {
  private String language;
  private String region;
  private String quoteType;
  private String quoteSourceName;
  private Boolean triggerable;
  private String currency;
  private Double ytdReturn;
  private Double trailingThreeMonthReturns;
  private Double trailingThreeMonthNavReturns;
  private Long sharesOutstanding;
  private Double fiftyDayAverage;
  private Double fiftyDayAverageChange;
  private Double fiftyDayAverageChangePercent;
  private Double twoHundredDayAverage;
  private Double twoHundredDayAverageChange;
  private Double twoHundredDayAverageChangePercent;
  private Long marketCap;
  private Integer sourceIntegererval;
  private Integer exchangeDataDelayedBy;
  private Boolean tradeable;
  private String exchange;
  private String LongName;
  private String messageBoardId;
  private String exchangeTimezoneName;
  private String exchangeTimezoneShortName;
  private Integer gmtOffSetMilliseconds;
  private String market;
  private Boolean esgPopulated;
  private String marketState;
  private String shortName;
  private Long firstTradeDateMilliseconds;
  private Integer priceHInteger;
  private Double postMarketChangePercent;
  private Integer postMarketTime;
  private Double postMarketPrice;
  private Double postMarketChange;
  private Double regularMarketChange;
  private Double regularMarketChangePercent;
  private Integer regularMarketTime;
  private Double regularMarketPrice;
  private Double regularMarketDayHigh;
  private String regularMarketDayRange;
  private Double regularMarketDayLow;
  private Integer regularMarketVolume;
  private Double regularMarketPreviousClose;
  private Double bid;
  private Double ask;
  private Integer bidSize;
  private Integer askSize;
  private String fullExchangeName;
  private String financialCurrency;
  private Double regularMarketOpen;
  private Integer averageDailyVolume3Month;
  private Integer averageDailyVolume10Day;
  private Double fiftyTwoWeekLowChange;
  private Double fiftyTwoWeekLowChangePercent;
  private String fiftyTwoWeekRange;
  private Double fiftyTwoWeekHighChange;
  private Double fiftyTwoWeekHighChangePercent;
  private Double fiftyTwoWeekLow;
  private Double fiftyTwoWeekHigh;
  private String symbol;
}