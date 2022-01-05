package co.alphavantage;

import co.alphavantage.service.AVantageQueryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger.ErrorLogger;
import feign.Logger.Level;
import feign.Request.Options;
import feign.codec.JacksonDecoder;
import feign.codec.JacksonEncoder;
import java.util.concurrent.TimeUnit;

public class AVantageServiceRun {

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
        .target(AVantageQueryApi.class, "https://www.alphavantage.co");
    var service = new AVantageQueryServiceImpl(api, args[0]);

//    var res = service.companyOverview("MSFT");
//    System.out.println(res);

    var csv = service.activeListing();
    System.out.println(csv);
  }
}