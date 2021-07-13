package me.devtools4.telegram.controller;

import lombok.extern.slf4j.Slf4j;
import me.devtools4.telegram.service.TickerWebHookBot;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
public class WebHookController {

  private final TickerWebHookBot bot;

  public WebHookController(TickerWebHookBot bot) {
    this.bot = bot;
  }

  @RequestMapping(value = "/webhook", method = RequestMethod.POST)
  public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
    return bot.onWebhookUpdateReceived(update);
  }
}