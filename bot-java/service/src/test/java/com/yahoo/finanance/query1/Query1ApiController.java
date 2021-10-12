package com.yahoo.finanance.query1;

import static me.devtools4.telegram.TestOps.res2str;

import java.time.temporal.ChronoUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Query1ApiController {

  @GetMapping("/v7/finance/quote")
  public String quote(@RequestParam("symbols") String symbol) {
    return res2str("data/" + symbol.toUpperCase() + ".json");
  }

  @GetMapping("/v7/finance/download/{symbol}")
  public String history(
      @PathVariable("symbol") String symbol,
      @RequestParam("interval") String interval,
      @RequestParam("period1") Long period1,
      @RequestParam("period2") Long period2) {
    var t1 = Query1Api.time(period1);
    var t2 = Query1Api.time(period2);
    var diff = ChronoUnit.DAYS.between(t1, t2);
    var name = diff > 300 ?
        "data/" + symbol.toUpperCase() + "-1y.csv" :
        "data/" + symbol.toUpperCase() + "-1m.csv";
    return res2str(name);
  }
}