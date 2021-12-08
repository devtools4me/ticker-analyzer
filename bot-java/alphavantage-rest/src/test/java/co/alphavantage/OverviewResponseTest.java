package co.alphavantage;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OverviewResponseTest {

  @Test
  public void testAapl() throws IOException {
    try (var is = getClass().getClassLoader().getResourceAsStream("AAPL.json")) {
      assertNotNull(is);
      var res = new ObjectMapper().readValue(is, OverviewResponse.class);
      assertNotNull(res);
      assertThat(res.getMarketCapitalizationM().toString(), is("2655211.0"));
      assertThat(res.getNdToEBITDA().toString(), is("-0.01"));
      System.out.println(res);
    }
  }

  @Test
  public void testCflt() throws IOException {
    try (var is = getClass().getClassLoader().getResourceAsStream("CFLT.json")) {
      assertNotNull(is);
      var res = new ObjectMapper().readValue(is, OverviewResponse.class);
      assertNotNull(res);
      assertThat(res.getMarketCapitalizationM().toString(), is("19222.0"));
      assertThat(Optional.ofNullable(res.getNdToEBITDA()), is(Optional.empty()));
      System.out.println(res);
    }
  }
}