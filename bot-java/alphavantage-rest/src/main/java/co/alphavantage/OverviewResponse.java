package co.alphavantage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.function.Function;
import me.devtools4.telegram.jackson.DoubleDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;
import lombok.Data;
import lombok.ToString;
import me.devtools4.telegram.jackson.LongDeserializer;

@Data
@ToString
@JsonInclude(Include.NON_NULL)
public class OverviewResponse {
  @JsonProperty("Symbol")
  public String symbol;
  @JsonProperty("AssetType")
  public String assetType;
  @JsonProperty("Name")
  public String name;
  @JsonProperty("Description")
  public String description;
  @JsonProperty("CIK")
  public String cik;
  @JsonProperty("Exchange")
  public String exchange;
  @JsonProperty("Currency")
  public String currency;
  @JsonProperty("Country")
  public String country;
  @JsonProperty("Sector")
  public String sector;
  @JsonProperty("Industry")
  public String industry;
  @JsonProperty("Address")
  public String address;
  @JsonProperty("FiscalYearEnd")
  public String fiscalYearEnd;
  @JsonProperty("LatestQuarter")
  public String latestQuarter;

  @JsonProperty("MarketCapitalization")
  @JsonDeserialize(using = LongDeserializer.class)
  public Long marketCapitalization;

  @JsonProperty("EBITDA")
  @JsonDeserialize(using = LongDeserializer.class)
  public Long ebitda;

  @JsonProperty("PERatio")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double peRatio;

  @JsonProperty("PEGRatio")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double pegRatio;

  @JsonProperty("BookValue")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double bookValue;

  @JsonProperty("DividendPerShare")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double dividendPerShare;

  @JsonProperty("DividendYield")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double dividendYield;

  @JsonProperty("EPS")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double eps;

  @JsonProperty("RevenuePerShareTTM")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double revenuePerShareTTM;

  @JsonProperty("ProfitMargin")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double profitMargin;

  @JsonProperty("OperatingMarginTTM")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double operatingMarginTTM;

  @JsonProperty("ReturnOnAssetsTTM")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double returnOnAssetsTTM;

  @JsonProperty("ReturnOnEquityTTM")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double returnOnEquityTTM;

  @JsonProperty("RevenueTTM")
  @JsonDeserialize(using = LongDeserializer.class)
  public Long revenueTTM;

  @JsonProperty("GrossProfitTTM")
  @JsonDeserialize(using = LongDeserializer.class)
  public Long grossProfitTTM;

  @JsonProperty("DilutedEPSTTM")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double dilutedEPSTTM;

  @JsonProperty("QuarterlyEarningsGrowthYOY")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double quarterlyEarningsGrowthYOY;

  @JsonProperty("QuarterlyRevenueGrowthYOY")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double quarterlyRevenueGrowthYOY;

  @JsonProperty("AnalystTargetPrice")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double analystTargetPrice;

  @JsonProperty("TrailingPE")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double trailingPE;

  @JsonProperty("ForwardPE")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double forwardPE;

  @JsonProperty("PriceToSalesRatioTTM")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double priceToSalesRatioTTM;

  @JsonProperty("PriceToBookRatio")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double priceToBookRatio;

  @JsonProperty("EVToRevenue")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double evToRevenue;

  @JsonProperty("EVToEBITDA")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double evToEBITDA;

  @JsonProperty("Beta")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double beta;

  @JsonProperty("52WeekHigh")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double the52WeekHigh;

  @JsonProperty("52WeekLow")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double the52WeekLow;

  @JsonProperty("50DayMovingAverage")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double the50DayMovingAverage;

  @JsonProperty("200DayMovingAverage")
  @JsonDeserialize(using = DoubleDeserializer.class)
  public Double the200DayMovingAverage;

  @JsonProperty("SharesOutstanding")
  @JsonDeserialize(using = LongDeserializer.class)
  public Long sharesOutstanding;

  @JsonProperty("DividendDate")
  public String dividendDate;
  @JsonProperty("ExDividendDate")
  public String exDividendDate;

  public Double getMarketCapitalizationM() {
    return Optional.ofNullable(marketCapitalization)
        .map(OverviewResponse::long2doubleM)
        .orElse(Double.NaN);
  }

  public Double getLaRatio() {
    return null;
  }

  public Double getNdToEBITDA() {
    return evToEBITDA != null && marketCapitalization != null && ebitda != null ?
      scale(evToEBITDA - long2double(marketCapitalization, ebitda, 2), 2) :
      null;
  }

  public Function<String, Double> percent() {
    return v -> Optional.ofNullable(v)
        .filter(x -> !x.isBlank())
        .map(Double::parseDouble)
        .map(OverviewResponse::percentOf)
        .orElse(null);
  }

  public static Boolean isValid(OverviewResponse other) {
    return Optional.ofNullable(other)
        .filter(x -> x.getSymbol() != null && x.getAssetType() != null && x.getName() != null)
        .isPresent();
  }

  public static Double billionsOf(Long x) {
    return x != null ? long2doubleB(x) : null;
  }

  public static Double percentOf(Double x) {
    return x != null ? scale(x * 100, 2) : null;
  }

  private static double long2doubleM(long x) {
    return long2double(x, Math.pow(10, 6), 2);
  }

  private static double long2doubleB(long x) {
    return long2double(x, Math.pow(10, 9), 2);
  }

  private static double long2double(long x, double divider, int scale) {
    return scale(
        new BigDecimal(x).divide(BigDecimal.valueOf(divider), MathContext.DECIMAL32),
        scale
    );
  }

  private static double scale(double x, int scale) {
    return scale(BigDecimal.valueOf(x), scale);
  }

  private static double scale(BigDecimal x, int scale) {
    return x.setScale(scale, RoundingMode.HALF_EVEN)
        .doubleValue();
  }
}