package me.devtools4.telegram.df.chart;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import me.devtools4.telegram.df.PngProps;
import org.apache.commons.io.IOUtils;

public class ChartStrategyRun {

  public static void main(String[] args) throws IOException {
//    var strategy = new JoinChartStrategy(new EmaChartStrategy(), new ApoChartStrategy());
//    String fname = "ema-apo.png";
    var strategy = new MacdChartStrategy();
    String fname = "macd.png";
    png(strategy, fname);
  }

  private static void png(ChartStrategy strategy, String fname) throws IOException {
    try (var is = ChartStrategyRun.class.getClassLoader().getResourceAsStream("QCOM.csv");
        var os = new FileOutputStream(fname, false))
    {
      var csv = IOUtils.toString(is, Charset.defaultCharset());
      strategy.png(csv, PngProps.builder()
          .rowKeyColumnName("Date")
          .columnName("Adj Close")
          .width(600)
          .height(500)
          .build(), os);
      os.flush();
    }
  }
}