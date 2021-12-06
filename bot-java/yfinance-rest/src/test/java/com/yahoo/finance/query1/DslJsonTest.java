package com.yahoo.finance.query1;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.dslplatform.json.DslJson;
import com.yahoo.finanance.query1.QuoteResponseResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class DslJsonTest {

  @Test
  public void testIntc() throws IOException {
    try (var is = getClass().getClassLoader().getResourceAsStream("INTC.json")) {
      assertNotNull(is);
      var res = new DslJson<>().deserialize(QuoteResponseResponse.class, is);
      assertNotNull(res);
      System.out.println(res);
    }
  }

  @Test
  public void testMsft() throws IOException {
    try (var is = getClass().getClassLoader().getResourceAsStream("MSFT.json")) {
      assertNotNull(is);
      var res = new DslJson<>().deserialize(QuoteResponseResponse.class, is);
      assertNotNull(res);
      System.out.println(res);
    }
  }
}