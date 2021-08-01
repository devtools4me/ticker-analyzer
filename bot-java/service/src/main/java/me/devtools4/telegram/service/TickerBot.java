package me.devtools4.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class TickerBot extends TelegramLongPollingBot implements ApiMethodConsumer {

  private final String userName;
  private final String token;
  private final CommandHandler handler;

  public TickerBot(String userName, String token, CommandHandler handler) {
    this.userName = userName;
    this.token = token;
    this.handler = handler;
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
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      log.info("update={}", update.getMessage());

      var text = update.getMessage().getText();
      var chatId = update.getMessage().getChatId();
      try {
        handler.handle(chatId.toString(), text, this);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
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