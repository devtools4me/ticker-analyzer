package me.devtools4.telegram.config;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.yahoo.finanance.query1.Query1Api;
import me.devtools4.telegram.controller.TickerRequestInterceptor;
import me.devtools4.telegram.service.CommandHandler;
import me.devtools4.telegram.service.MustacheRender;
import me.devtools4.telegram.service.StringToPeriodConverter;
import me.devtools4.telegram.service.TickerService;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class TickerConfig {

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