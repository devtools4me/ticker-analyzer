package me.devtools4.telegram.controller;

import com.yahoo.finanance.query1.Quote;
import me.devtools4.telegram.api.Period;
import me.devtools4.telegram.api.TickerApi;
import me.devtools4.telegram.service.TickerService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<Resource> history(@PathVariable("id") String id) {
    return ok(id, service.history(id, Period.OneMonth));
  }

  @Override
  @GetMapping(TickerApi.HISTORY_PERIOD_ID)
  public ResponseEntity<Resource> history(@PathVariable("id") String id, @PathVariable("period") Period period) {
    return ok(id, service.history(id, period));
  }

  @Override
  @GetMapping(TickerApi.SMA_ID)
  public ResponseEntity<Resource> sma(@PathVariable("id") String id) {
    return ok(id, service.sma(id, Period.OneMonth));
  }

  @Override
  @GetMapping(TickerApi.SMA_PERIOD_ID)
  public ResponseEntity<Resource> sma(@PathVariable("id") String id, @PathVariable("period") Period period) {
    return ok(id, service.sma(id, period));
  }

  private static ResponseEntity<Resource> ok(String id, byte[] bytes) {
    return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".png\"")
        .body(new ByteArrayResource(bytes));
  }
}
