package com.yahoo.finanance.query1;

import com.dslplatform.json.CompiledJson;
import java.util.List;
import lombok.Data;

@Data
@CompiledJson
public class QuoteResponse {
  private List<Quote> result;
  private Error error;
}