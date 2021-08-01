package me.devtools4.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class TickerWebHookBot extends TelegramWebhookBot {

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
      log.info("update={}", update);

      var text = update.getMessage().getText();
      var chatId = update.getMessage().getChatId();
      try {
        handler.handle(this, chatId.toString(), text);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (update.hasCallbackQuery()) {
      var data = update.getCallbackQuery().getData();
      var messageId = update.getCallbackQuery().getMessage().getMessageId();
      var chatId = update.getCallbackQuery().getMessage().getChatId();
      handler.query(this, chatId.toString(), messageId, data);
    }
    return null;
  }
}