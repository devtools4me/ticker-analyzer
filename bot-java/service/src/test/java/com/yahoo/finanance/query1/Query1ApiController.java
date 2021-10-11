package com.yahoo.finanance.query1;

import static me.devtools4.telegram.TestOps.res2str;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Query1ApiController {

  @GetMapping("/v7/finance/quote")
  public String quote(@RequestParam("symbols") String symbol) {
    return res2str("data/" + symbol.toUpperCase() + ".json");
  }
}