package me.devtools4.telegram.service;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TickerRegister {
  private final TelegramBotsApi api;
  private final TickerBot bot;

  public TickerRegister(TelegramBotsApi api, TickerBot bot) {
    this.api = api;
    this.bot = bot;
  }

  public void register() throws TelegramApiException {
    api.registerBot(bot);
  }
}