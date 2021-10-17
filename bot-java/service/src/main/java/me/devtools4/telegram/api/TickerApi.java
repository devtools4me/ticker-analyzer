package me.devtools4.telegram.api;

import com.yahoo.finanance.query1.Quote;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface TickerApi {

  String START = "/start";
  String QUOTE = "/quote";
  String QUOTE_ID = QUOTE + "/{id}";
  String STRATEGY_ID = "/{strategy}/{id}";
  String STRATEGY_PERIOD_ID = "/{strategy}/{period}/{id}";

  List<StartInfo> start();

  Quote quote(String id);

  ResponseEntity<Resource> strategy(String strategy, String id);

  ResponseEntity<Resource> strategy(String strategy, String id, Period period);
}