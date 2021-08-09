package me.devtools4.telegram.config;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.yahoo.finanance.query1.Query1Api;
import java.io.FileInputStream;
import me.devtools4.telegram.controller.TickerRequestInterceptor;
import me.devtools4.telegram.df.EtfRepository;
import me.devtools4.telegram.service.CommandHandler;
import me.devtools4.telegram.service.MustacheRender;
import me.devtools4.telegram.service.StringToPeriodConverter;
import me.devtools4.telegram.service.TickerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class TickerConfig {

  @Value("${etf.csv}")
  private String etfCsv;

  @Bean
  public CommandHandler commandHandler(TickerService tickerService, MustacheRender mustacheRender) {
    return new CommandHandler(tickerService, mustacheRender);
  }

  @Bean
  public MustacheRender mustacheRender() {
    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache m = mf.compile("quote.mustache");
    Mustache error = mf.compile("error.mustache");
    return new MustacheRender(m, error);
  }

  @Bean
  public TickerService tickerService(Query1Api query1Api, EtfRepository etfRepository) {
    return new TickerService(query1Api, etfRepository);
  }

  @Bean
  public EtfRepository etfRepository() throws Exception {
    try (var is = new FileInputStream(etfCsv)) {
      return new EtfRepository(is);
    }
  }

  @Bean
  public WebMvcConfigurer webMvcConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TickerRequestInterceptor());
      }

      @Override
      public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToPeriodConverter());
      }
    };
  }
}