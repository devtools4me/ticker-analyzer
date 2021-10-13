package me.devtools4.telegram.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class WebhookBotTemplate extends TelegramWebhookBot implements ApiMethodConsumer {
  private final Logger log = LoggerFactory.getLogger(getClass());

  protected final CommandHandler handler;

  public WebhookBotTemplate(CommandHandler handler) {
    this.handler = handler;
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
}