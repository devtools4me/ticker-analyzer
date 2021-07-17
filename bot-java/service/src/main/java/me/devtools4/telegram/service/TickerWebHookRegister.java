package me.devtools4.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class TickerWebHookRegister {
  private final TelegramBotsApi api;
  private final TickerWebHookBot bot;
  private final SetWebhook setWebhook;

  public TickerWebHookRegister(TelegramBotsApi api, TickerWebHookBot bot,
      SetWebhook setWebhook) {
    this.api = api;
    this.bot = bot;
    this.setWebhook = setWebhook;
  }

  public void register() throws TelegramApiException {
    log.info("Register webhook");
    api.registerBot(bot, setWebhook);
  }
}