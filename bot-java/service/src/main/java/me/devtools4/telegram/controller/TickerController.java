package me.devtools4.telegram.controller;

import com.yahoo.finanance.query1.Query1Api;
import com.yahoo.finanance.query1.Quote;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TickerController {

  private final Query1Api api;

  public TickerController(Query1Api api) {
    this.api = api;
  }

  @GetMapping("/ticker/{id}")
  public Quote ticker(@PathVariable("id") String id) throws IOException {
    return api.quote(id)
        .getQuoteResponse()
        .getResult()
        .stream()
        .findFirst()
        .orElse(null);
  }
}
