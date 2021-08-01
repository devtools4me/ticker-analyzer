package me.devtools4.telegram.service;

import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface ApiMethodConsumer {
  void accept(SendChatAction t);

  void accept(SendMessage t);

  void accept(SendPhoto t);

  void accept(EditMessageText t);
}