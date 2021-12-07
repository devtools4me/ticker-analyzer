package me.devtools4.telegram.config;

import static me.devtools4.telegram.api.Command.APO;
import static me.devtools4.telegram.api.Command.BLSH;
import static me.devtools4.telegram.api.Command.EMA;
import static me.devtools4.telegram.api.Command.HISTORY;
import static me.devtools4.telegram.api.Command.MACD;
import static me.devtools4.telegram.api.Command.MOM;
import static me.devtools4.telegram.api.Command.SMA;

import co.alphavantage.service.AVantageQueryService;
import com.github.mustachejava.DefaultMustacheFactory;
import com.yahoo.finanance.query1.Query1Api;
import java.io.FileInputStream;
import java.util.Map;
import me.devtools4.telegram.controller.TickerRequestInterceptor;
import me.devtools4.telegram.df.EtfRepository;
import me.devtools4.telegram.df.chart.ApoChartStrategy;
import me.devtools4.telegram.df.chart.BlshChartStrategy;
import me.devtools4.telegram.df.chart.EmaChartStrategy;
import me.devtools4.telegram.df.chart.HistoryChartStrategy;
import me.devtools4.telegram.df.chart.MacdChartStrategy;
import me.devtools4.telegram.df.chart.MomChartStrategy;
import me.devtools4.telegram.df.chart.SmaChartStrategy;
import me.devtools4.telegram.service.CommandHandler;
import me.devtools4.telegram.service.MustacheRender;
import me.devtools4.telegram.service.MustacheRender.TemplateType;
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
    var mf = new DefaultMustacheFactory();
    return new MustacheRender(Map.of(
        TemplateType.Quote, mf.compile("quote.mustache"),
        TemplateType.Mul, mf.compile("mul.mustache"),
        TemplateType.Error, mf.compile("error.mustache")
    ));
  }

  @Bean
  public TickerService tickerService(Query1Api query1Api, AVantageQueryService avQueryService, EtfRepository etfRepository) {
    return new TickerService(query1Api, avQueryService, etfRepository, Map.of(
        HISTORY, new HistoryChartStrategy(),
        SMA, new SmaChartStrategy(),
        EMA, new EmaChartStrategy(),
        APO, new ApoChartStrategy(),
        MACD, new MacdChartStrategy(),
        MOM, new MomChartStrategy(),
        BLSH, new BlshChartStrategy()
    ));
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