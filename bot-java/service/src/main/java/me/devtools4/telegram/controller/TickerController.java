package me.devtools4.telegram.controller;

import com.yahoo.finanance.query1.Quote;
import me.devtools4.telegram.api.Period;
import me.devtools4.telegram.api.TickerApi;
import me.devtools4.telegram.service.TickerService;
import org.bouncycastle.util.encoders.Base64;
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

  @Override
  @GetMapping(TickerApi.HISTORY_ID)
  public String history(@PathVariable("id") String id) {
    var bytes = service.history(id, Period.OneMonth);
    return new String(Base64.encode(bytes));
  }

  @Override
  @GetMapping(TickerApi.HISTORY_PERIOD_ID)
  public String history(@PathVariable("id") String id, @PathVariable("period") Period period) {
    var bytes = service.history(id, period);
    return new String(Base64.encode(bytes));
  }
}
