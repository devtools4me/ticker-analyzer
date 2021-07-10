package me.devtools4.telegram.config;

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
import me.devtools4.telegram.service.TickerBot;
import me.devtools4.telegram.service.TickerRegister;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TickerConfig {
  @Value("${bot.userName}")
  private String userName;

  @Value("${bot.token}")
  private String token;

  @Bean
  public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
    return new TelegramBotsApi(DefaultBotSession.class);
  }

  @Bean
  public TickerBot tickerBot() {
    return new TickerBot(userName, token);
  }

  @Bean(initMethod = "register")
  public TickerRegister tickerRegister(TelegramBotsApi telegramBotsApi, TickerBot tickerBot) {
    return new TickerRegister(telegramBotsApi, tickerBot);
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
        .target(Query1Api.class, "https://query1.finance.yahoo.com");
  }
}