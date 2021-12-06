package me.devtools4.telegram.config;

import feign.codec.DslJsonDecoder;
import feign.codec.DslJsonEncoder;
import com.yahoo.finanance.query1.Query1Api;
import com.yahoo.finanance.query1.YFinanceExceptionDecoder;
import feign.Feign;
import feign.Logger.ErrorLogger;
import feign.Logger.Level;
import feign.Request.Options;
import feign.codec.Decoder;
import feign.codec.Encoder;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Profile("!test")
public class QueryConfig {

  @Value("${yahoo.finance.query1.url}")
  private String query1Url;

  @Bean
  public Query1Api query1Api() {
    final Decoder decoder = new DslJsonDecoder();
    final Encoder encoder = new DslJsonEncoder();
    return Feign.builder()
        .encoder(encoder)
        .decoder(decoder)
        .errorDecoder(new YFinanceExceptionDecoder(decoder))
        .logger(new ErrorLogger())
        .logLevel(Level.BASIC)
        .options(new Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
        .target(Query1Api.class, query1Url);
  }
}