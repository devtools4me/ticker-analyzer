package me.devtools4.telegram.df;

import com.d3x.morpheus.frame.DataFrame;
import com.d3x.morpheus.frame.DataFrameValue;
import com.d3x.morpheus.viz.chart.Chart;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataFrameFuncsRun {

  public static void main(String[] args) throws IOException {
    try (var is = DataFrameFuncsRun.class.getClassLoader().getResourceAsStream("GLD.csv")) {
      var df = DataFrame.read(is).csv(LocalDate.class, x -> {
        x.setRowKeyColumnName("Date");
      });
      df.head(5).forEach(System.out::println);
      System.out.println(df.count(x -> true));
      df.min(x -> x.colKey().equals("Close"))
          .map(DataFrameValue::getValue)
          .ifPresent(System.out::println);
      var dfc = df.col("Close");
      var stats = dfc.stats();
      System.out.println(stats.mean());
      System.out.println(stats.percentile(0.95));
      System.out.println(stats.variance());
      System.out.println(stats.stdDev());

      df.out().print();

      var close = df.cols()
          .select(column -> column.key().equalsIgnoreCase("Close"));
//      Chart.create().asHtml().withLinePlot(close, chart -> {
//        chart.title().withText("Close");
//        chart.plot().axes().domain().label().withText("Date");
//        chart.plot().axes().range(0).label().withText("Price");
//        chart.show();
//      });

      System.out.println(dfc.count(x -> true));

      Chart.create().asSwing().withLinePlot(close, chart -> {
        chart.title().withText("Close");
        chart.plot().axes().domain().label().withText("Date");
        chart.plot().axes().range(0).label().withText("Price");
//        chart.show();
        chart.writerPng(new File("test.png"), 500, 500, false);
      });
    }
  }
}