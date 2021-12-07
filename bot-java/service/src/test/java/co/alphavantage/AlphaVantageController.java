package co.alphavantage;

import static me.devtools4.telegram.TestOps.res2str;

import co.alphavantage.AVantageQueryApi.Function;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlphaVantageController {

  @GetMapping("/query")
  public String query(@RequestParam("function") Function function, @RequestParam("symbol") String symbol, @RequestParam("apikey") String apiKey) {
    return res2str("data/av/" + symbol.toUpperCase() + ".json");
  }
}