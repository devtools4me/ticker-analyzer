package me.devtools4.telegram.api;

import com.yahoo.finanance.query1.Quote;

public interface TickerApi {

  String QUOTE = "/quote";
  String QUOTE_ID = QUOTE + "/{id}";
  String HISTORY = "/history";
  String HISTORY_ID = HISTORY + "/{id}";

  Quote quote(String id);
  String history(String id);
}