package me.devtools4.telegram.service;

import static me.devtools4.telegram.api.TickerApi.HISTORY;
import static me.devtools4.telegram.api.TickerApi.QUOTE;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.devtools4.telegram.api.Period;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    if (text.startsWith(QUOTE)) {
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
}