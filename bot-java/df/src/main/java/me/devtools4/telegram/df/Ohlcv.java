package me.devtools4.telegram.df;

import com.d3x.morpheus.frame.DataFrame;
import com.d3x.morpheus.frame.DataFrameCursor;
import com.d3x.morpheus.viz.chart.Chart;
import com.d3x.morpheus.viz.chart.ChartShape;
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

  public void blshPng(OutputStream os, PngProps props) throws IOException {
    try (var is = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))) {
      var df = DataFrame.read(is).csv(LocalDate.class, x -> {
        x.setRowKeyColumnName(props.getRowKeyColumnName());
      });

      var columnName = props.getColumnName();
      var close = df.cols()
          .select(column -> column.key().equalsIgnoreCase(columnName));

      var min = close.cols()
          .stats().rolling(2).min()
          .cols()
          .replaceKey(columnName, "Diff");

      var diff = DataFrame.concatColumns(close, min);
      diff.col("Diff").applyDoubles(value -> {
        var current = (Double) ((DataFrameCursor) value).col(columnName).getValue();
        var minimum = (Double) ((DataFrameCursor) value).col("Diff").getValue();
        return minimum.isNaN() || current.equals(minimum) ? 0.0 : 1.0;
      });
      diff = diff.cols()
          .select(column -> column.key().equalsIgnoreCase("Diff"));

      var diff2 = DataFrame.concatColumns(
          diff,
          diff.cols()
              .stats().rolling(2).min()
              .cols().replaceKey("Diff", "Min"));

      diff2.col("Min").applyDoubles(value -> {
        var current = (Double) ((DataFrameCursor) value).col("Diff").getValue();
        var minimum = (Double) ((DataFrameCursor) value).col("Min").getValue();
        if (minimum.isNaN()) {
          return Double.NaN;
        } else if (current < minimum) {
          return -1.0;
        } else if (current.equals(minimum)) {
          return 0.0;
        } else {
          return 1.0;
        }
      });
      diff2.cols().replaceKey("Min", "Diff2");

      var blsh = DataFrame.concatColumns(close, diff2);

      Chart.create().asSwing().withLinePlot(blsh, chart -> {
        chart.title().withText("Close");
        chart.plot().axes().domain().label().withText("Date");
        chart.plot().axes().range(0).label().withText("Price");
        chart.plot().style(columnName).withLineWidth(1f).withColor(Color.BLUE);
        chart.plot().style("Diff2").withPointShape(ChartShape.TRIANGLE_DOWN).withColor(Color.GREEN);
        chart.legend().on().bottom();
        chart.writerPng(os, props.getWidth(), props.getHeight(), false);
      });
    }
  }
}