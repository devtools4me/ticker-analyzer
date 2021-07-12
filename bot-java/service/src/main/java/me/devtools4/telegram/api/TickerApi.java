package me.devtools4.telegram.api;

import com.yahoo.finanance.query1.Quote;

public interface TickerApi {

  String QUOTE = "/quote";
  String QUOTE_ID = QUOTE + "/{id}";

  Quote quote(String id);
}