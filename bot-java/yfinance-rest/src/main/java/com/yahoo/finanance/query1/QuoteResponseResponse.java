package com.yahoo.finanance.query1;

import com.dslplatform.json.CompiledJson;
import lombok.Data;

@Data
@CompiledJson
public class QuoteResponseResponse {
  private QuoteResponse quoteResponse;
}