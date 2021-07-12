package me.devtools4.telegram.controller;

import com.yahoo.finanance.query1.Quote;
import me.devtools4.telegram.api.TickerApi;
import me.devtools4.telegram.service.TickerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TickerController implements TickerApi {

  private final TickerService service;

  public TickerController(TickerService service) {
    this.service = service;
  }

  @Override
  @GetMapping(TickerApi.QUOTE_ID)
  public Quote quote(@PathVariable("id") String id) {
    return service.quote(id);
  }
}
