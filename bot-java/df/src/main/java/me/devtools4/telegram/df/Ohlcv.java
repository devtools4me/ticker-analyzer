package me.devtools4.telegram.df;

import com.d3x.morpheus.frame.DataFrame;
import com.d3x.morpheus.viz.chart.Chart;
import java.awt.Color;
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

  public void png(OutputStream os, PngProps props) throws IOException {
    try (var is = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))) {
      var df = DataFrame.read(is).csv(LocalDate.class, x -> {
        x.setRowKeyColumnName(props.getRowKeyColumnName());
      });

      var columnName = props.getColumnName();
      var close = df.cols()
          .select(column -> column.key().equalsIgnoreCase(columnName));

      Chart.create().asSwing().withLinePlot(close, chart -> {
        chart.title().withText("Close");
        chart.plot().style("Close").withLineWidth(1f).withColor(Color.BLUE);
        chart.plot().axes().domain().label().withText("Date");
        chart.plot().axes().range(0).label().withText("Price");
        chart.writerPng(os, props.getWidth(), props.getHeight(), false);
      });
    }
  }

  public void smaPng(OutputStream os, PngProps props) throws IOException {
    try (var is = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))) {
      var df = DataFrame.read(is).csv(LocalDate.class, x -> {
        x.setRowKeyColumnName(props.getRowKeyColumnName());
      });

      var columnName = props.getColumnName();
      var close = df.cols()
          .select(column -> column.key().equalsIgnoreCase(columnName));

      var shortSize = 40;
      var st = close
          .cols()
          .stats().rolling(shortSize).mean()
          .cols()
          .replaceKey(columnName, "Short Term");

      var longSize = 70;
      var lt = close
          .cols()
          .stats().rolling(longSize).mean()
          .cols()
          .replaceKey(columnName, "Long Term");

      var sam = DataFrame.concatColumns(close, st, lt);

      Chart.create().asSwing().withLinePlot(sam, chart -> {
        chart.title().withText("Close");
        chart.plot().axes().domain().label().withText("Date");
        chart.plot().axes().range(0).label().withText("Price");
        chart.plot().style(columnName).withLineWidth(1f).withColor(Color.BLUE);
        chart.plot().style("Short Term").withLineWidth(1f).withColor(Color.GREEN);
        chart.plot().style("Long Term").withLineWidth(1f).withColor(Color.ORANGE);
        chart.legend().on().bottom();
        chart.writerPng(os, props.getWidth(), props.getHeight(), false);
      });
    }
  }
}