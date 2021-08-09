package me.devtools4.telegram.df;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class EtfRepositoryTest {

  private static final String CSV =
      "Symbol,Annual Return,Months,Years,Start Date,Close Price (start),End Date,Close Price (end),Name,Segment,Issuer,Expense Ratio,AUM\n"
          +
          "SOGU,-95.36,1,0.08,2021-05-19,30.03,2021-06-16,23.26,The Short De-SPAC ETF,Inverse Equity: U.S. - Total Market,Tuttle Tactical Management LLC,0.95%,$8.64M\n"
          +
          "HIBS,-90.81,19,1.58,2019-11-07,493.70,2021-06-16,11.27,Direxion Daily S&P 500 High Beta Bear 3X Shares,Inverse Equity: U.S. - Large Cap,Direxion,1.03%,$18.16M\n"
          +
          "UVXY,-84.43,116,9.67,2011-10-04,2058000000.00,2021-06-16,31.94,ProShares Ultra VIX Short-Term Futures ETF,Leveraged Alternatives: U.S. - Volatility,ProShares,0.95%,$833.88M\n";

  private EtfRepository sut;

  @Before
  public void before() {
    sut = EtfRepository.of(CSV);
  }

  @Test
  public void testFind() {
    var opt = sut.find("sogu");
    assertThat(opt.isPresent(), is(true));
    opt.ifPresent(x -> {
      assertThat(x.getName(), is("The Short De-SPAC ETF"));
      assertThat(x.getSegment(), is("Inverse Equity: U.S. - Total Market"));
      assertThat(x.getIssuer(), is("Tuttle Tactical Management LLC"));
      assertThat(x.getExpenseRatio(), is("0.95%"));
      assertThat(x.getAum(), is("$8.64M"));
    });
  }

  @Test
  public void testNotFound() {
    var opt = sut.find("sogu1");
    assertThat(opt.isPresent(), is(false));
  }
}