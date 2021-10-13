package me.devtools4.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class TickerWebHookBot extends WebhookBotTemplate {

  private final String userName;
  private final String token;
  private final String botPath;

  public TickerWebHookBot(String userName, String token, String botPath, CommandHandler handler) {
    super(handler);
    this.userName = userName;
    this.token = token;
    this.botPath = botPath;
  }

  @Override
  public String getBotUsername() {
    return userName;
  }

  @Override
  public String getBotToken() {
    return token;
  }

  @Override
  public String getBotPath() {
    return botPath;
  }

  @Override
  public void onRegister() {
    log.info("TickerWebHookBot has been registered, botPath={}", botPath);
  }

  @Override
  public void accept(SendChatAction t) {
    try {
      execute(t);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void accept(SendMessage t) {
    try {
      execute(t);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void accept(SendPhoto t) {
    try {
      execute(t);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void accept(EditMessageText t) {
    try {
      execute(t);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
}