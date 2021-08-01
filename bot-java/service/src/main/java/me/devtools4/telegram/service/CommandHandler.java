package me.devtools4.telegram.service;

import static me.devtools4.telegram.api.TickerApi.HISTORY;
import static me.devtools4.telegram.api.TickerApi.QUOTE;
import static me.devtools4.telegram.api.TickerApi.START;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.devtools4.telegram.api.Period;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class CommandHandler {

  private static final List<String> patterns = Arrays.stream(Period.values())
      .map(Period::getPeriod)
      .map(x -> HISTORY + "/" + x)
      .collect(Collectors.toList());

  private final TickerService service;
  private final MustacheRender render;

  public CommandHandler(TickerService service, MustacheRender render) {
    this.service = service;
    this.render = render;
  }

  public void handle(DefaultAbsSender sender, String chatId, String text)
      throws TelegramApiException {
    var typing = new SendChatAction();
    typing.setChatId(chatId);
    typing.setAction(ActionType.TYPING);
    sender.execute(typing);

    if (text.startsWith(START)) {
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
                  .build()
          ))
          .build());
      sender.execute(message);
    } else if (text.startsWith(QUOTE)) {
      var id = text.replace(QUOTE + "/", "");
      var quote = service.quote(id);
      var html = render.html(quote);
      var message = new SendMessage();
      message.setChatId(chatId);
      message.setText(html);
      message.setParseMode("HTML");
      sender.execute(message);
    } else if (patterns.stream().anyMatch(text::startsWith)) {
      var p = patterns.stream()
          .filter(text::startsWith)
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException(text));
      var period = Period.convert(p.replace(HISTORY + "/", ""));
      var id = text.replace(p + "/", "");
      var bytes = service.history(id, period);
      var message = new SendPhoto();
      message.setChatId(chatId);
      message.setPhoto(new InputFile(new ByteArrayInputStream(bytes), id + ".png"));
      sender.execute(message);
    } else if (text.startsWith(HISTORY)) {
      var id = text.replace(HISTORY + "/", "");
      var bytes = service.history(id, Period.OneMonth);
      var message = new SendPhoto();
      message.setChatId(chatId);
      message.setPhoto(new InputFile(new ByteArrayInputStream(bytes), id + ".png"));
      sender.execute(message);
    }
  }

  public void query(DefaultAbsSender sender, String chatId, Integer messageId, String data) {
    Optional<String> answer = Optional.empty();
    if (QUOTE.equals(data)) {
      answer = Optional.of("quote.html");
    } else if (HISTORY.equals(data)) {
      answer = Optional.of("history.html");
    }
    answer.ifPresent(x -> {
      try (var is = getClass().getClassLoader().getResourceAsStream(x)) {
        var message = EditMessageText.builder()
            .chatId(chatId)
            .messageId(messageId)
            .text(IOUtils.toString(is, Charset.defaultCharset()))
            .parseMode("HTML")
            .build();
        sender.execute(message);
      } catch (Exception ex) {
        log.warn("{}, error={}", x, ex.getMessage(), ex);
      }
    });
  }
}