package me.devtools4.telegram.config;

import me.devtools4.telegram.service.CommandHandler;
import me.devtools4.telegram.service.TickerWebHookBot;
import me.devtools4.telegram.service.TickerWebHookRegister;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Profile("tele-webhook")
public class TelegramWebHookConfig {
  @Value("${bot.userName}")
  private String userName;

  @Value("${bot.token}")
  private String token;

  @Value("${bot.botPath}")
  private String botPath;

  @Bean
  public TickerWebHookBot tickerWebHookBot(CommandHandler commandHandler) {
    return new TickerWebHookBot(userName, token, botPath, commandHandler);
  }

  @Bean(initMethod = "register")
  public TickerWebHookRegister tickerWebHookRegister(
      TelegramBotsApi telegramBotsApi, TickerWebHookBot tickerBot, SetWebhook setWebhook) {
    return new TickerWebHookRegister(telegramBotsApi, tickerBot, setWebhook);
  }

  @Bean
  public SetWebhook setWebhook() {
    return SetWebhook.builder()
        .dropPendingUpdates(true)
        .maxConnections(5)
//        .ipAddress()
        .build();
  }
}