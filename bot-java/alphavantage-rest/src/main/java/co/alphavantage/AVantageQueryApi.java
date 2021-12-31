package co.alphavantage;

import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface AVantageQueryApi {
  enum Function {
    OVERVIEW,
    LISTING_STATUS
  }

  @RequestLine("GET /query?function={function}&symbol={symbol}&apikey={apiKey}")
  OverviewResponse query(@Param("function") Function function, @Param("symbol") String symbol, @Param("apiKey") String apiKey);

  @RequestLine("GET /query?function=LISTING_STATUS&apikey={apiKey}")
  Response listing(@Param("apiKey") String apiKey);
}