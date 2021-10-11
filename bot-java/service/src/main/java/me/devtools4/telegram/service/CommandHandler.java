package me.devtools4.telegram.service;

import static me.devtools4.telegram.api.TickerApi.BLSH;
import static me.devtools4.telegram.api.TickerApi.HISTORY;
import static me.devtools4.telegram.api.TickerApi.QUOTE;
import static me.devtools4.telegram.api.TickerApi.SMA;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import me.devtools4.aops.annotations.Trace;
import me.devtools4.telegram.api.Command;
import me.devtools4.telegram.api.Period;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Slf4j
public class CommandHandler {

  private final TickerService service;
  private final MustacheRender render;

  public CommandHandler(TickerService service, MustacheRender render) {
    this.service = service;
    this.render = render;
  }

  @Trace(level = "INFO")
  public void handle(String chatId, String text, ApiMethodConsumer consumer) {
    var typing = new SendChatAction();
    typing.setChatId(chatId);
    typing.setAction(ActionType.TYPING);
    consumer.accept(typing);

    try {
      var cmd = Command.of(text);
      var params = cmd.params(text);
      switch (cmd) {
        case START: {
          var message = new SendMessage();
          message.setChatId(chatId);
          message.setText("What would you like to receive?");
          message.setReplyMarkup(InlineKeyboardMarkup.builder()
              .keyboardRow(List.of(
                  InlineKeyboardButton.builder()
                      .text(QUOTE)
                      .callbackData(QUOTE)
                      .build(),
                  InlineKeyboardButton.builder()
                      .text(HISTORY)
                      .callbackData(HISTORY)
                      .build(),
                  InlineKeyboardButton.builder()
                      .text(SMA)
                      .callbackData(SMA)
                      .build(),
                  InlineKeyboardButton.builder()
                      .text(BLSH)
                      .callbackData(BLSH)
                      .build()
              ))
              .build());
          consumer.accept(message);
          break;
        }
        case QUOTE: {
          var quote = service.quote(params.get("id"));
          var html = render.html(quote);
          var message = new SendMessage();
          message.setChatId(chatId);
          message.setText(html);
          message.setParseMode("HTML");
          consumer.accept(message);
          break;
        }
        case HISTORY: {
          var id = params.get("id");
          var period = params.containsKey("period") ?
              Period.convert(params.get("period")) :
              Period.OneMonth;
          var bytes = service.history(id, period);
          var message = new SendPhoto();
          message.setChatId(chatId);
          message.setPhoto(new InputFile(new ByteArrayInputStream(bytes), id + ".png"));
          consumer.accept(message);
          break;
        }
        case SMA: {
          var id = params.get("id");
          var period = params.containsKey("period") ?
              Period.convert(params.get("period")) :
              Period.OneMonth;
          var bytes = service.sma(id, period);
          var message = new SendPhoto();
          message.setChatId(chatId);
          message.setPhoto(new InputFile(new ByteArrayInputStream(bytes), id + ".png"));
          consumer.accept(message);
          break;
        }
        case BLSH: {
          var id = params.get("id");
          var period = params.containsKey("period") ?
              Period.convert(params.get("period")) :
              Period.OneMonth;
          var bytes = service.blsh(id, period);
          var message = new SendPhoto();
          message.setChatId(chatId);
          message.setPhoto(new InputFile(new ByteArrayInputStream(bytes), id + ".png"));
          consumer.accept(message);
          break;
        }
        default:
          throw new IllegalArgumentException("cmd=" + cmd + ", text=" + text);
      }
    } catch (Throwable ex) {
      log.warn("Error: {}", ex.getMessage(), ex);

      var error = render.error(ex);
      var message = new SendMessage();
      message.setChatId(chatId);
      message.setText(error);
      message.setParseMode("HTML");
      consumer.accept(message);
    }
  }

  @Trace(level = "INFO")
  public void query(String chatId, Integer messageId, String data, ApiMethodConsumer consumer) {
    Optional<String> answer = Optional.empty();
    var cmd = Command.of(data);
    switch (cmd) {
      case QUOTE: {
        answer = Optional.of("quote.html");
        break;
      }
      case HISTORY: {
        answer = Optional.of("history.html");
        break;
      }
      case SMA: {
        answer = Optional.of("sma.html");
        break;
      }
    }
    answer.ifPresent(x -> {
      try (var is = getClass().getClassLoader().getResourceAsStream(x)) {
        var message = EditMessageText.builder()
            .chatId(chatId)
            .messageId(messageId)
            .text(IOUtils.toString(is, Charset.defaultCharset()))
            .parseMode("HTML")
            .build();
        consumer.accept(message);
      } catch (Exception ex) {
        log.warn("{}, error={}", x, ex.getMessage(), ex);
      }
    });
  }
}