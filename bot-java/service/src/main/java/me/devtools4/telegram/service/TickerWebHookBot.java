package me.devtools4.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class TickerWebHookBot extends TelegramWebhookBot implements ApiMethodConsumer {

  private final String userName;
  private final String token;
  private final String botPath;
  private final CommandHandler handler;

  public TickerWebHookBot(String userName, String token, String botPath, CommandHandler handler) {
    this.userName = userName;
    this.token = token;
    this.botPath = botPath;
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
  public String getBotPath() {
    return botPath;
  }

  @Override
  public void onRegister() {
    log.info("TickerWebHookBot has been registered, botPath={}", botPath);
  }

  @Override
  public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      log.info("update={}", update.getMessage());

      var text = update.getMessage().getText();
      var chatId = update.getMessage().getChatId();
      handler.handle(chatId.toString(), text, this);
    } else if (update.hasCallbackQuery()) {
      var data = update.getCallbackQuery().getData();
      var messageId = update.getCallbackQuery().getMessage().getMessageId();
      var chatId = update.getCallbackQuery().getMessage().getChatId();
      handler.query(chatId.toString(), messageId, data, this);
    }
    return null;
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