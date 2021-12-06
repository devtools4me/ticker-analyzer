package com.yahoo.finance.query1;

import static com.yahoo.finanance.query1.Query1Api.bodyAsString;
import static com.yahoo.finanance.query1.Query1Api.timestamp;

import feign.codec.DslJsonDecoder;
import feign.codec.DslJsonEncoder;
import com.yahoo.finanance.query1.Query1Api;
import com.yahoo.finanance.query1.YFinanceExceptionDecoder;
import feign.Feign;
import feign.Logger.ErrorLogger;
import feign.Logger.Level;
import feign.Request.Options;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.Encoder;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class Query1ApiRun {

  public static void main(String args[]) throws IOException {

    final Decoder decoder = new DslJsonDecoder();
    final Encoder encoder = new DslJsonEncoder();
    var api = Feign.builder()
        .encoder(encoder)
        .decoder(decoder)
        .errorDecoder(new YFinanceExceptionDecoder(decoder))
        .logger(new ErrorLogger())
        .logLevel(Level.BASIC)
        .options(new Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
        .target(Query1Api.class, "https://query1.finance.yahoo.com");

    try {
      api.quote("GLD")
          .getQuoteResponse()
          .getResult()
          .forEach(System.out::println);
    } catch (Exception ex) {
      System.out.println("Error=" + ex.getMessage());
    }
    var from = LocalDateTime.now();

    Response res = api.download("GDL", "1d",
        timestamp(LocalDateTime.now().minus(1, ChronoUnit.MONTHS)),
        timestamp(LocalDateTime.now()));
    System.out.println(bodyAsString(res));
  }
}