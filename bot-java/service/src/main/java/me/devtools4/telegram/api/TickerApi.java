package me.devtools4.telegram.api;

import co.alphavantage.OverviewResponse;
import com.yahoo.finanance.query1.Quote;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface TickerApi {

  String START = "/start";
  String QUOTE = "/quote";
  String QUOTE_ID = QUOTE + "/{id}";
  String MUL = "/mul";
  String MUL_ID = MUL + "/{id}";
  String CMP = "/cmp";
  String CMP_IDS = CMP + "/{ids}";
  String STRATEGY_ID = "/{strategy}/{id}";
  String STRATEGY_PERIOD_ID = "/{strategy}/{period}/{id}";

  List<StartInfo> start();

  Quote quote(String id);

  OverviewResponse multipliers(String id);

  ResponseEntity<Resource> compare(String ids);

  ResponseEntity<Resource> strategy(String strategy, String id, Indicator indicator);

  ResponseEntity<Resource> strategy(String strategy, String id, Period period, Indicator indicator);
}