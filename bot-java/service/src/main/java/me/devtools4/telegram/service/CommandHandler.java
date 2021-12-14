package me.devtools4.telegram.service;

import static co.alphavantage.OverviewResponse.isValid;

import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.devtools4.aops.annotations.Trace;
import me.devtools4.telegram.api.Command;
import me.devtools4.telegram.api.Indicator;
import me.devtools4.telegram.api.Ops;
import me.devtools4.telegram.api.Period;
import me.devtools4.telegram.service.MustacheRender.TemplateType;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
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
      var params = Command.params(text);
      switch (cmd) {
        case START: {
          var message = new SendMessage();
          message.setChatId(chatId);
          message.setText("What would you like to receive?");
          var builder = InlineKeyboardMarkup.builder();
          Lists.partition(service.start(), 3)
              .forEach(p -> builder
                  .keyboardRow(p.stream()
                      .map(x -> InlineKeyboardButton.builder()
                          .text(x.getText())
                          .callbackData(x.getCallbackData())
                          .build())
                      .collect(Collectors.toList())));
          message.setReplyMarkup(builder.build());
          consumer.accept(message);
          break;
        }
        case QUOTE: {
          var quote = service.quote(params.get("id"));
          var html = render.html(quote, TemplateType.Quote);
          var message = new SendMessage();
          message.setChatId(chatId);
          message.setText(html);
          message.setParseMode("HTML");
          consumer.accept(message);
          break;
        }
        case MUL: {
          var id = params.get("id");
          var overview = service.multipliers(id);
          if (!isValid(overview)) {
            throw new IllegalArgumentException("Invalid response, id=" + id + ", response=" + overview.toString());
          }

          var html = render.html(overview, TemplateType.Mul);
          var message = new SendMessage();
          message.setChatId(chatId);
          message.setText(html);
          message.setParseMode("HTML");
          consumer.accept(message);
          break;
        }
        case CMP: {
          var ids = new HashSet<>(List.of(params.get("id").split("&")));
          var bytes = service.compare(ids);
          var fileName = ids.stream().reduce("", Ops.reduce("_")) + ".pdf";
          var message = new SendDocument();
          message.setChatId(chatId);
          message.setDocument(new InputFile(new ByteArrayInputStream(bytes), fileName));
          consumer.accept(message);
          break;
        }
        case HISTORY:
        case SMA:
        case EMA:
        case MACD:
        case MOM:
        case BLSH: {
          var id = params.get("id");
          var period = params.containsKey("period") ?
              Period.convert(params.get("period")) :
              Period.OneMonth;
          var indicator = Optional.ofNullable(params.get("indicator"))
              .map(String::toUpperCase)
              .map(Indicator::valueOf)
              .orElse(null);
          var bytes = service.png(cmd, id, period, indicator);
          var message = new SendPhoto();
          message.setChatId(chatId);
          message.setPhoto(new InputFile(new ByteArrayInputStream(bytes), id + ".png"));
          consumer.accept(message);
          break;
        }
        default:
          throw new IllegalArgumentException("Unsupported cmd=" + cmd + ", text=" + text);
      }
    } catch (Throwable ex) {
      log.warn("Error: {}", ex.getMessage(), ex);

      var error = render.html(ex, TemplateType.Error);
      var message = new SendMessage();
      message.setChatId(chatId);
      message.setText(error);
      message.setParseMode("HTML");
      consumer.accept(message);
    }
  }

  @Trace(level = "INFO")
  public void query(String chatId, Integer messageId, String data, ApiMethodConsumer consumer) {
    Optional.of(Command.of(data))
        .filter(x -> !Command.START.is(x) && !Command.UNKNOWN.is(x))
        .map(x -> x.getPath().replace("/", "") + ".html")
        .ifPresent(x -> {
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