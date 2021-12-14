package me.devtools4.telegram.controller;

import lombok.extern.slf4j.Slf4j;
import me.devtools4.telegram.service.WebhookBotTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
//@Profile("webhook-bot")
public class WebHookController {

  private final WebhookBotTemplate bot;

  public WebHookController(WebhookBotTemplate bot) {
    this.bot = bot;
  }

  @RequestMapping(value = "/callback/webhook", method = RequestMethod.POST)
  public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
    return bot.onWebhookUpdateReceived(update);
  }
}