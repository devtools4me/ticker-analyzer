package co.alphavantage;

import feign.Param;
import feign.RequestLine;

public interface AVantageQueryApi {
  enum Function {
    OVERVIEW
  }

  @RequestLine("GET /query?function={function}&symbol={symbol}&apikey={apiKey}")
  OverviewResponse query(@Param("function") Function function, @Param("symbol") String symbol, @Param("apiKey") String apiKey);
}