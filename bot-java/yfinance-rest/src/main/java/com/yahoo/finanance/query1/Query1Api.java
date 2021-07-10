package com.yahoo.finanance.query1;

import feign.Param;
import feign.RequestLine;

public interface Query1Api {

  @RequestLine("GET /v7/finance/quote?symbols={symbol_id}")
  QuoteResponseResponse quote(@Param("symbol_id") String symbol_id);
}