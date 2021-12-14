package me.devtools4.telegram.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public class TestWebhookBot extends WebhookBotTemplate {
  private List<Object> list = new CopyOnWriteArrayList<>();

  public TestWebhookBot(CommandHandler handler) {
    super(handler);
  }

  public void clear() {
    list.clear();
  }

  @SuppressWarnings("unchecked")
  public <T> T get(int i) {
    return (T)list.get(i);
  }

  @Override
  public void accept(SendChatAction t) {
    list.add(t);
  }

  @Override
  public void accept(SendMessage t) {
    list.add(t);
  }

  @Override
  public void accept(SendPhoto t) {
    list.add(t);
  }

  @Override
  public void accept(SendDocument t) {
    list.add(t);
  }

  @Override
  public void accept(EditMessageText t) {
    list.add(t);
  }

  @Override
  public String getBotUsername() {
    return "TEST";
  }

  @Override
  public String getBotToken() {
    return "TEST";
  }

  @Override
  public String getBotPath() {
    return "TEST";
  }
}