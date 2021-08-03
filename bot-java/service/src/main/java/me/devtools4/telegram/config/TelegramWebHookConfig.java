package me.devtools4.telegram.config;

import lombok.extern.slf4j.Slf4j;
import me.devtools4.telegram.service.CommandHandler;
import me.devtools4.telegram.service.TickerWebHookBot;
import me.devtools4.telegram.service.TickerWebHookRegister;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.Webhook;
import org.telegram.telegrambots.meta.generics.WebhookBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Profile("webhook-bot")
public class TelegramWebHookConfig {
  @Value("${bot.userName}")
  private String userName;

  @Value("${bot.token}")
  private String token;

  @Value("${bot.url}")
  private String url;

  @Value("${bot.botPath}")
  private String botPath;

  @Bean
  public TickerWebHookBot tickerWebHookBot(CommandHandler commandHandler) {
    return new TickerWebHookBot(userName, token, botPath, commandHandler);
  }

  @Bean(initMethod = "register")
  public TickerWebHookRegister tickerWebHookRegister(TelegramBotsApi telegramBotsApi,
      TickerWebHookBot tickerBot, SetWebhook setWebhook) {
    return new TickerWebHookRegister(telegramBotsApi, tickerBot, setWebhook);
  }

  @Bean
  public SetWebhook setWebhook() {
    return SetWebhook.builder()
        .dropPendingUpdates(true)
        .maxConnections(5)
        .url(url)
        .build();
  }

  @Bean
  public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
    return new TelegramBotsApi(DefaultBotSession.class, new Webhook() {
      @Override
      public void startServer() throws TelegramApiException {
        log.info("startServer");
      }

      @Override
      public void registerWebhook(WebhookBot callback) {
        log.info("registerWebhook, botPath={}", callback.getBotPath());
      }

      @Override
      public void setInternalUrl(String internalUrl) {
        log.info("setInternalUrl, internalUrl={}", internalUrl);
      }

      @Override
      public void setKeyStore(String keyStore, String keyStorePassword) throws TelegramApiException {
        log.info("setKeyStore, keyStore={}", keyStore);
      }
    });
  }
}