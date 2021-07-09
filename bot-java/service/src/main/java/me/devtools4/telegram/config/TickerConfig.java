package me.devtools4.telegram.config;

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
}