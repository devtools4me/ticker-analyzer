package com.yahoo.finanance.query1;

import feign.Param;
import feign.RequestLine;
import feign.Response;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.apache.commons.io.IOUtils;

public interface Query1Api {
  String DAY = "1d";
  String WEEK = "5d";
  String MONTH = "1mo";

  @RequestLine("GET /v7/finance/quote?symbols={symbol_id}")
  QuoteResponseResponse quote(@Param("symbol_id") String symbol_id);

  @RequestLine("GET /v7/finance/download/{symbol_id}?interval={interval}&period1={from_date}&period2={to_date}")
  Response download(
      @Param("symbol_id") String symbol_id,
      @Param("interval") String interval,
      @Param("from_date") Long from_date,
      @Param("to_date") Long to_date);

  static long timestamp(LocalDateTime time) {
    return Date.from(time.atZone(ZoneId.systemDefault()).toInstant()).getTime() / 1000;
  }

  static LocalDateTime time(long timestamp) {
    var d = new Date(timestamp * 1000);
    return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
  }

  static String bodyAsString(Response res) {
    try (var is = res.body().asInputStream()) {
      return IOUtils.toString(is, Charset.defaultCharset());
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}