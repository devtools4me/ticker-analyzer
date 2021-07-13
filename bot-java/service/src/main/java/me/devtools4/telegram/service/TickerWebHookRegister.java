package me.devtools4.telegram.service;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    api.registerBot(bot, setWebhook);
  }
}