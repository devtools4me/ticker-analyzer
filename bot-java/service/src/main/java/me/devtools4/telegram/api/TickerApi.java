package me.devtools4.telegram.api;

import com.yahoo.finanance.query1.Quote;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface TickerApi {

  String START = "/start";
  String QUOTE = "/quote";
  String QUOTE_ID = QUOTE + "/{id}";
  String HISTORY = "/history";
  String HISTORY_ID = HISTORY + "/{id}";
  String HISTORY_PERIOD = "/history/{period}";
  String HISTORY_PERIOD_ID = HISTORY_PERIOD + "/{id}";
  String SMA = "/sma";
  String SMA_ID = SMA + "/{id}";
  String SMA_PERIOD = "/sma/{period}";
  String SMA_PERIOD_ID = SMA_PERIOD + "/{id}";
  String BLSH = "/blsh";
  String BLSH_ID = BLSH + "/{id}";
  String BLSH_PERIOD = "/blsh/{period}";
  String BLSH_PERIOD_ID = BLSH_PERIOD + "/{id}";

  Quote quote(String id);

  ResponseEntity<Resource> history(String id);

  ResponseEntity<Resource> history(String id, Period period);

  ResponseEntity<Resource> sma(String id);

  ResponseEntity<Resource> sma(String id, Period period);

  ResponseEntity<Resource> blsh(String id);

  ResponseEntity<Resource> blsh(String id, Period period);
}