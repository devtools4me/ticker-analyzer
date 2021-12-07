package me.devtools4.telegram.config;

import co.alphavantage.AlphaVantageExceptionDecoder;
import co.alphavantage.AVantageQueryApi;
import co.alphavantage.service.AVantageQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger.ErrorLogger;
import feign.Logger.Level;
import feign.Request.Options;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.JacksonDecoder;
import feign.codec.JacksonEncoder;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class AlphaVantageConfig {

  @Value("${alpha.vantage.url}")
  private String url;

  @Value("${alpha.vantage.token}")
  private String token;

  @Bean
  public AVantageQueryService avQueryService(AVantageQueryApi api) {
    return new AVantageQueryService(api, token);
  }

  @Bean
  public AVantageQueryApi avQueryApi(ObjectMapper mapper) {
    final Decoder decoder = new JacksonDecoder(mapper);
    final Encoder encoder = new JacksonEncoder(mapper);
    return Feign.builder()
        .encoder(encoder)
        .decoder(decoder)
        .errorDecoder(new AlphaVantageExceptionDecoder(decoder))
        .logger(new ErrorLogger())
        .logLevel(Level.BASIC)
        .options(new Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
        .target(AVantageQueryApi.class, url);
  }
}