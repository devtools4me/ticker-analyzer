package com.yahoo.finance.query1;

import com.dslplatform.json.DslJson;
import com.yahoo.finanance.query1.QuoteResponseResponse;
import java.io.IOException;
import org.junit.Test;

public class DslJsonTest {

  @Test
  public void test() throws IOException {
    try (var is = getClass().getClassLoader().getResourceAsStream("GLD.json")) {
      new DslJson<>().deserialize(QuoteResponseResponse.class, is);
    }
  }
}