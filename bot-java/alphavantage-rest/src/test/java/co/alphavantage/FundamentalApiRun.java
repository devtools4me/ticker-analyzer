package co.alphavantage;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger.ErrorLogger;
import feign.Logger.Level;
import feign.Request.Options;
import feign.codec.JacksonDecoder;
import feign.codec.JacksonEncoder;
import java.util.concurrent.TimeUnit;

public class FundamentalApiRun {

  public static void main(String args[]) {
    var mapper = new ObjectMapper();
    var decoder = new JacksonDecoder(mapper);
    var encoder = new JacksonEncoder(mapper);
    var api = Feign.builder()
        .encoder(encoder)
        .decoder(decoder)
        .errorDecoder(new AlphaVantageExceptionDecoder(decoder))
        .logger(new ErrorLogger())
        .logLevel(Level.BASIC)
        .options(new Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
        .target(FundamentalApi.class, "https://www.alphavantage.co");

    var res = api.companyOverview("AAPL", args[0]);
    System.out.println(res);
  }
}