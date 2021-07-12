package me.devtools4.telegram.service;

import com.dslplatform.json.DslJson;
import com.github.mustachejava.DefaultMustacheFactory;
import com.yahoo.finanance.query1.QuoteResponseResponse;
import java.io.IOException;
import org.junit.Before;
import org.junit.jupiter.api.Test;

public class MustacheRenderTest {

  private final MustacheRender mustacheRender;

  public MustacheRenderTest() {
    var mf = new DefaultMustacheFactory();
    var m = mf.compile("quote.mustache");
    mustacheRender = new MustacheRender(m);
  }

  @Test
  public void test() throws IOException {
    try (var is = getClass().getClassLoader().getResourceAsStream("AAPL.json")) {
      var r = new DslJson<>().deserialize(QuoteResponseResponse.class, is);
      var q = r.getQuoteResponse().getResult().get(0);
      System.out.println(q);
      var out = mustacheRender.html(q);
      System.out.println(out);
    }
  }
}
