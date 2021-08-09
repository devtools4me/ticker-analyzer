package me.devtools4.telegram.service;

import com.dslplatform.json.DslJson;
import com.github.mustachejava.DefaultMustacheFactory;
import com.yahoo.finanance.query1.QuoteResponseResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class MustacheRenderTest {

  private final MustacheRender mustacheRender;

  public MustacheRenderTest() {
    var mf = new DefaultMustacheFactory();
    var m = mf.compile("quote.mustache");
    var error = mf.compile("error.mustache");
    mustacheRender = new MustacheRender(m, error);
  }

  @Test
  public void testHtml() throws IOException {
    try (var is = getClass().getClassLoader().getResourceAsStream("INTC.json")) {
      var r = new DslJson<>().deserialize(QuoteResponseResponse.class, is);
      var q = r.getQuoteResponse().getResult().get(0);
      q.setExpenseRatio("0.95%");
      q.setAum("$8.64M");
      System.out.println(q);
      var out = mustacheRender.html(q);
      System.out.println(out);
    }
  }

  @Test
  public void testError() {
    var out = mustacheRender.error(new IllegalArgumentException("TEST"));
    System.out.println(out);
  }
}
