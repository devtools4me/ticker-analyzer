package co.alphavantage;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class OverviewResponseTest {

  @Test
  public void testAapl() throws IOException {
    try (var is = getClass().getClassLoader().getResourceAsStream("AAPL.json")) {
      assertNotNull(is);
      var res = new ObjectMapper().readValue(is, OverviewResponse.class);
      assertNotNull(res);
      System.out.println(res);
    }
  }
}