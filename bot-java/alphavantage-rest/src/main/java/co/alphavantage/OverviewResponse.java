package co.alphavantage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
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
  public String marketCapitalization;
  @JsonProperty("EBITDA")
  public String ebitda;
  @JsonProperty("PERatio")
  public String peRatio;
  @JsonProperty("PEGRatio")
  public String pegRatio;
  @JsonProperty("BookValue")
  public String bookValue;
  @JsonProperty("DividendPerShare")
  public String dividendPerShare;
  @JsonProperty("DividendYield")
  public String dividendYield;
  @JsonProperty("EPS")
  public String eps;
  @JsonProperty("RevenuePerShareTTM")
  public String revenuePerShareTTM;
  @JsonProperty("ProfitMargin")
  public String profitMargin;
  @JsonProperty("OperatingMarginTTM")
  public String operatingMarginTTM;
  @JsonProperty("ReturnOnAssetsTTM")
  public String returnOnAssetsTTM;
  @JsonProperty("ReturnOnEquityTTM")
  public String returnOnEquityTTM;
  @JsonProperty("RevenueTTM")
  public String revenueTTM;
  @JsonProperty("GrossProfitTTM")
  public String grossProfitTTM;
  @JsonProperty("DilutedEPSTTM")
  public String dilutedEPSTTM;
  @JsonProperty("QuarterlyEarningsGrowthYOY")
  public String quarterlyEarningsGrowthYOY;
  @JsonProperty("QuarterlyRevenueGrowthYOY")
  public String quarterlyRevenueGrowthYOY;
  @JsonProperty("AnalystTargetPrice")
  public String analystTargetPrice;
  @JsonProperty("TrailingPE")
  public String trailingPE;
  @JsonProperty("ForwardPE")
  public String forwardPE;
  @JsonProperty("PriceToSalesRatioTTM")
  public String priceToSalesRatioTTM;
  @JsonProperty("PriceToBookRatio")
  public String priceToBookRatio;
  @JsonProperty("EVToRevenue")
  public String evToRevenue;
  @JsonProperty("EVToEBITDA")
  public String evToEBITDA;
  @JsonProperty("Beta")
  public String beta;
  @JsonProperty("52WeekHigh")
  public String the52WeekHigh;
  @JsonProperty("52WeekLow")
  public String the52WeekLow;
  @JsonProperty("50DayMovingAverage")
  public String the50DayMovingAverage;
  @JsonProperty("200DayMovingAverage")
  public String the200DayMovingAverage;
  @JsonProperty("SharesOutstanding")
  public String sharesOutstanding;
  @JsonProperty("DividendDate")
  public String dividendDate;
  @JsonProperty("ExDividendDate")
  public String exDividendDate;
}