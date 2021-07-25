package me.devtools4.telegram.df;

import com.d3x.morpheus.frame.DataFrame;
import com.d3x.morpheus.viz.chart.Chart;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class Ohlcv {

  private final String csv;

  public Ohlcv(String csv) {
    this.csv = csv;
  }

  public void png(OutputStream os, int width, int height) {
    try (var is = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))) {
      var df = DataFrame.read(is).csv(LocalDate.class, x -> {
        x.setRowKeyColumnName("Date");
      });

      var close = df.cols()
          .select(column -> column.key().equalsIgnoreCase("Close"));

      Chart.create().asSwing().withLinePlot(close, chart -> {
        chart.title().withText("Close");
        chart.plot().axes().domain().label().withText("Date");
        chart.plot().axes().range(0).label().withText("Price");
        chart.writerPng(os, width, height, false);
      });
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }
}