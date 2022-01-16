package me.devtools4.telegram.api;

import static co.alphavantage.OverviewResponse.billionsOf;
import static co.alphavantage.OverviewResponse.percentOf;

import co.alphavantage.OverviewResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Multipliers {
  public static List<String> COLUMNS = List.of(
      "Symbol", "Name", "Sector",
      "P", "EV/EBITDA", "ND/EBITDA", "P/E", "ROE, %", "L/A", "ROA, %", "P/S", "P/B",
      "Free-float",
      "Dividend/Share", "Dividend Yield, %", "Dividend Date", "Ex Dividend Date");

  private String symbol;
  private String name;
  private String sector;
  private Long p;
  private Double evToEbitdaRatio;
  private Double ndToEbitdaRatio;
  private Double peRatio;
  private Double roe;
  private Double laRatio;
  private Double roa;
  private Double psRatio;
  private Double pbRatio;
  private Double freeFloat;
  private Double dividendPerShare;
  private Double dividendYield;
  private String dividendDate;
  private String exDividendDate;

  public List<String> str() {
    return List.of(symbol, name, sector,
        str(billionsOf(p)) + "B", str(evToEbitdaRatio), str(ndToEbitdaRatio),
        str(peRatio), str(roe), str(laRatio), str(roa), str(psRatio), str(pbRatio),
        str(freeFloat),
        str(dividendPerShare), str(dividendYield), dividendDate, exDividendDate);
  }

  public static Multipliers of(OverviewResponse x) {
    return Multipliers.builder()
        .symbol(x.getSymbol())
        .name(x.getName())
        .sector(x.getSector())
        .p(x.getMarketCapitalization())
        .evToEbitdaRatio(x.getEvToEBITDA())
        .ndToEbitdaRatio(x.getNdToEBITDA())
        .peRatio(x.getPeRatio())
        .roe(percentOf(x.getReturnOnEquityTTM()))
        //.laRatio()
        .roa(percentOf(x.getReturnOnAssetsTTM()))
        .psRatio(x.getPriceToSalesRatioTTM())
        .pbRatio(x.getPriceToBookRatio())
        //.freeFloat()
        .dividendPerShare(x.getDividendPerShare())
        .dividendYield(percentOf(x.getDividendYield()))
        .dividendDate(x.getDividendDate())
        .exDividendDate(x.getExDividendDate())
        .build();
  }

  private static <T> String str(T t) {
    return Optional.ofNullable(t).map(Objects::toString).orElse("");
  }
}