package me.devtools4.telegram.config;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.yahoo.finanance.query1.DslJsonDecoder;
import com.yahoo.finanance.query1.DslJsonEncoder;
import com.yahoo.finanance.query1.Query1Api;
import com.yahoo.finanance.query1.YFinanceExceptionDecoder;
import feign.Feign;
import feign.Logger.ErrorLogger;
import feign.Logger.Level;
import feign.Request.Options;
import feign.codec.Decoder;
import feign.codec.Encoder;
import java.util.concurrent.TimeUnit;
import me.devtools4.telegram.controller.TickerRequestInterceptor;
import me.devtools4.telegram.service.CommandHandler;
import me.devtools4.telegram.service.MustacheRender;
import me.devtools4.telegram.service.TickerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class TickerConfig {

  @Value("${yahoo.finance.query1.url}")
  private String query1Url;

  @Bean
  public CommandHandler commandHandler(TickerService tickerService, MustacheRender mustacheRender) {
    return new CommandHandler(tickerService, mustacheRender);
  }

  @Bean
  public MustacheRender mustacheRender() {
    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache m = mf.compile("quote.mustache");
    return new MustacheRender(m);
  }

  @Bean
  public TickerService tickerService(Query1Api query1Api) {
    return new TickerService(query1Api);
  }

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

  @Bean
  public WebMvcConfigurer webMvcConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TickerRequestInterceptor());
      }
    };
  }
}