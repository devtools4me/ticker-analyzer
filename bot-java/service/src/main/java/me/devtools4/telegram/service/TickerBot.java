package me.devtools4.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class TickerBot extends TelegramLongPollingBot {

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
      log.info("update={}", update);

      var text = update.getMessage().getText();
      var chatId = update.getMessage().getChatId();

      SendMessage message = new SendMessage();
      message.setChatId(chatId.toString());
      message.setText(handler.handle(text));
      message.setParseMode("html");
      try {
        execute(message);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}