package co.alphavantage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OverviewResponseTest {

  private static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of("AAPL.json", check(true,
            "OverviewResponse(symbol=AAPL, assetType=Common Stock, name=Apple Inc, description=Apple Inc. is an American multinational technology company that specializes in consumer electronics, computer software, and online services. Apple is the world's largest technology company by revenue (totalling $274.5 billion in 2020) and, since January 2021, the world's most valuable company. As of 2021, Apple is the world's fourth-largest PC vendor by unit sales, and fourth-largest smartphone manufacturer. It is one of the Big Five American information technology companies, along with Amazon, Google, Microsoft, and Facebook., cik=320193, exchange=NASDAQ, currency=USD, country=USA, sector=TECHNOLOGY, industry=ELECTRONIC COMPUTERS, address=ONE INFINITE LOOP, CUPERTINO, CA, US, fiscalYearEnd=September, latestQuarter=2021-09-30, marketCapitalization=2655211290000, ebitda=120233001000, peRatio=28.85, pegRatio=3.515, bookValue=3.841, dividendPerShare=0.85, dividendYield=0.0052, eps=5.61, revenuePerShareTTM=21.9, profitMargin=0.259, operatingMarginTTM=0.298, returnOnAssetsTTM=0.202, returnOnEquityTTM=1.474, revenueTTM=365817004000, grossProfitTTM=152836000000, dilutedEPSTTM=5.61, quarterlyEarningsGrowthYOY=0.662, quarterlyRevenueGrowthYOY=0.288, analystTargetPrice=168.95, trailingPE=28.85, forwardPE=29.33, priceToSalesRatioTTM=7.26, priceToBookRatio=42.59, evToRevenue=7.43, evToEBITDA=22.07, beta=1.206, the52WeekHigh=170.3, the52WeekLow=115.67, the50DayMovingAverage=150.1, the200DayMovingAverage=138.97, sharesOutstanding=16406400000, dividendDate=2021-11-11, exDividendDate=2021-11-05)",
            "2655211.0,-0.01")),
        Arguments.of("CFLT.json", check(true,
            "OverviewResponse(symbol=CFLT, assetType=Common Stock, name=Confluent Inc. Class A Common Stock, description=Confluent, Inc. is dedicated to developing a cloud-native platform for data in motion that helps companies connect their applications with real-time streams in the United States and internationally. The company is headquartered in Mountain View, California., cik=1699838, exchange=NASDAQ, currency=USD, country=USA, sector=TECHNOLOGY, industry=SERVICES-PREPACKAGED SOFTWARE, address=899 W. EVELYN AVENUE, MOUNTAIN VIEW, CA, UNITED STATES, fiscalYearEnd=December, latestQuarter=2021-09-30, marketCapitalization=19222000000, ebitda=null, peRatio=null, pegRatio=null, bookValue=3.673, dividendPerShare=null, dividendYield=null, eps=-0.984, revenuePerShareTTM=1.279, profitMargin=-0.769, operatingMarginTTM=-0.763, returnOnAssetsTTM=0.0, returnOnEquityTTM=0.0, revenueTTM=338260000, grossProfitTTM=161126000, dilutedEPSTTM=-0.984, quarterlyEarningsGrowthYOY=0.0, quarterlyRevenueGrowthYOY=0.668, analystTargetPrice=92.47, trailingPE=null, forwardPE=null, priceToSalesRatioTTM=57.15, priceToBookRatio=19.79, evToRevenue=53.91, evToEBITDA=null, beta=null, the52WeekHigh=94.97, the52WeekLow=37.71, the50DayMovingAverage=72.48, the200DayMovingAverage=60.35, sharesOutstanding=69539000, dividendDate=None, exDividendDate=None)",
            "19222.0,null")),
        Arguments.of("ZXZZT.json", check(true,
            "OverviewResponse(symbol=ZXZZT, assetType=Common Stock, name=NASDAQ TEST STOCK, description=NASDAQ TEST STOCK, cik=None, exchange=NASDAQ, currency=USD, country=USA, sector=TECHNOLOGY, industry=GENERAL, address=NONE, fiscalYearEnd=None, latestQuarter=0000-00-00, marketCapitalization=null, ebitda=null, peRatio=null, pegRatio=null, bookValue=null, dividendPerShare=null, dividendYield=0.0, eps=null, revenuePerShareTTM=0.0, profitMargin=0.0, operatingMarginTTM=0.0, returnOnAssetsTTM=0.0, returnOnEquityTTM=0.0, revenueTTM=0, grossProfitTTM=0, dilutedEPSTTM=0.0, quarterlyEarningsGrowthYOY=0.0, quarterlyRevenueGrowthYOY=0.0, analystTargetPrice=null, trailingPE=null, forwardPE=null, priceToSalesRatioTTM=null, priceToBookRatio=null, evToRevenue=null, evToEBITDA=null, beta=null, the52WeekHigh=18.08, the52WeekLow=6.13, the50DayMovingAverage=0.0, the200DayMovingAverage=0.0, sharesOutstanding=0, dividendDate=None, exDividendDate=None)",
            "NaN,null")),
        Arguments.of("HSI.json", check(false, "", "")));
  }

  private static Consumer<OverviewResponse> check(Boolean valid, String output, String exOutput) {
    return x -> {
      assertNotNull(x);
      assertThat(OverviewResponse.isValid(x), is(valid));
      if (OverviewResponse.isValid(x)) {
        assertThat(x.toString(), is(output));
        var ex = String.join(
            ",",
            List.of(
                String.valueOf(x.getMarketCapitalizationM()),
                String.valueOf(x.getNdToEBITDA()))
        );
        assertThat(ex, is(exOutput));
      }
    };
  }

  @ParameterizedTest
  @MethodSource("arguments")
  public void test(String path, Consumer<OverviewResponse> func) throws IOException {
    try (var is = getClass().getClassLoader().getResourceAsStream(path)) {
      assertNotNull(is);
      var res = new ObjectMapper().readValue(is, OverviewResponse.class);
      func.accept(res);
      //System.out.println(res);
    }
  }
}