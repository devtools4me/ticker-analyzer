package me.devtools4.telegram.service;

import com.dslplatform.json.DslJson;
import com.github.mustachejava.DefaultMustacheFactory;
import com.yahoo.finanance.query1.QuoteResponseResponse;
import java.io.IOException;
import java.util.Map;
import me.devtools4.telegram.service.MustacheRender.TemplateType;
import org.junit.jupiter.api.Test;

public class MustacheRenderTest {

  private final MustacheRender mustacheRender;

  public MustacheRenderTest() {
    var mf = new DefaultMustacheFactory();
    mustacheRender = new MustacheRender(Map.of(
        TemplateType.Quote, mf.compile("quote.mustache"),
        TemplateType.Mul, mf.compile("mul.mustache"),
        TemplateType.Error, mf.compile("error.mustache")
    ));
  }

  @Test
  public void testHtml() throws IOException {
    try (var is = getClass().getClassLoader().getResourceAsStream("INTC.json")) {
      var r = new DslJson<>().deserialize(QuoteResponseResponse.class, is);
      var q = r.getQuoteResponse().getResult().get(0);
      q.setExpenseRatio("0.95%");
      q.setAum("$8.64M");
      System.out.println(q);
      var out = mustacheRender.html(q, TemplateType.Quote);
      System.out.println(out);
    }
  }

  @Test
  public void testError() {
    var out = mustacheRender.html(new IllegalArgumentException("TEST"), TemplateType.Error);
    System.out.println(out);
  }
}
