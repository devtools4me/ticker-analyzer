package me.devtools4.telegram.controller;

import com.yahoo.finanance.query1.Quote;
import java.util.List;
import me.devtools4.telegram.api.Command;
import me.devtools4.telegram.api.Indicator;
import me.devtools4.telegram.api.Period;
import me.devtools4.telegram.api.StartInfo;
import me.devtools4.telegram.api.TickerApi;
import me.devtools4.telegram.service.TickerService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TickerController implements TickerApi {

  private final TickerService service;

  public TickerController(TickerService service) {
    this.service = service;
  }

  @Override
  @GetMapping(TickerApi.START)
  public List<StartInfo> start() {
    return service.start();
  }

  @Override
  @GetMapping(TickerApi.QUOTE_ID)
  public Quote quote(@PathVariable("id") String id) {
    return service.quote(id);
  }

  @Override
  @GetMapping(TickerApi.STRATEGY_ID)
  public ResponseEntity<Resource> strategy(@PathVariable("strategy") String strategy,
      @PathVariable("id") String id,
      @RequestParam(name = "i", required = false) Indicator indicator)
  {
    return ok(id, service.png(Command.of(strategy), id, Period.OneMonth, indicator));
  }

  @Override
  @GetMapping(TickerApi.STRATEGY_PERIOD_ID)
  public ResponseEntity<Resource> strategy(@PathVariable("strategy") String strategy,
      @PathVariable("id") String id,
      @PathVariable("period") Period period,
      @RequestParam(name = "i", required = false) Indicator indicator)
  {
    return ok(id, service.png(Command.of(strategy), id, period, indicator));
  }

  private static ResponseEntity<Resource> ok(String id, byte[] bytes) {
    return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".png\"")
        .body(new ByteArrayResource(bytes));
  }
}
