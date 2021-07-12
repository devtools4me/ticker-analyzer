package me.devtools4.telegram.service;

import static me.devtools4.telegram.api.TickerApi.QUOTE;

public class CommandHandler {

  private final TickerService service;
  private final MustacheRender render;

  public CommandHandler(TickerService service, MustacheRender render) {
    this.service = service;
    this.render = render;
  }

  public String handle(String text) {
    if (text.startsWith(QUOTE)) {
      var id = text.replace(QUOTE + "/", "");
      var quote = service.quote(id);
      return render.html(quote);
    } else {

    }

    return text;
  }
}