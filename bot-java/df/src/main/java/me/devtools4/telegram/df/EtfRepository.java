package me.devtools4.telegram.df;

import com.d3x.morpheus.frame.DataFrame;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EtfRepository {

  private final DataFrame<Integer, String> df;

  public EtfRepository(InputStream is) {
    df = DataFrame.read(is).csv();
  }

  public static EtfRepository of(String csv) {
    try (var is = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))) {
      return new EtfRepository(is);
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public Optional<Etf> find(String symbol) {
    return Optional.ofNullable(df.rows().select(y -> y.getValue("Symbol").equals(symbol.toUpperCase())))
        .filter(x -> x.rowCount() > 0)
        .map(x -> x.row(0))
        .map(x -> Etf.builder()
            .symbol(symbol)
            .startDate(x.getValue("Start Date"))
            .name(x.getValue("Name"))
            .segment(x.getValue("Segment"))
            .issuer(x.getValue("Issuer"))
            .expenseRatio(x.getValue("Expense Ratio"))
            .aum(x.getValue("AUM"))
            .build());
  }
}