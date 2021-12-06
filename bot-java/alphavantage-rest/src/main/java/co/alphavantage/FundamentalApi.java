package co.alphavantage;

import feign.Param;
import feign.RequestLine;

public interface FundamentalApi {
  @RequestLine("GET /query?function=OVERVIEW&symbol={symbol}&apikey={apiKey}")
  OverviewResponse companyOverview(@Param("symbol") String symbol, @Param("apiKey") String apiKey);
}