package me.devtools4.telegram.api;

import com.yahoo.finanance.query1.Quote;

public interface TickerApi {

  String START = "/start";
  String QUOTE = "/quote";
  String QUOTE_ID = QUOTE + "/{id}";
  String HISTORY = "/history";
  String HISTORY_ID = HISTORY + "/{id}";
  String HISTORY_PERIOD = "/history/{period}";
  String HISTORY_PERIOD_ID = HISTORY_PERIOD + "/{id}";

  Quote quote(String id);

  String history(String id);

  String history(String id, Period period);
}