package me.devtools4.telegram.df.chart;

import com.d3x.morpheus.frame.DataFrame;
import com.d3x.morpheus.frame.DataFrameCursor;
import com.d3x.morpheus.viz.chart.Chart;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;
import me.devtools4.telegram.df.PngProps;

public class EmaChartStrategy implements ChartStrategy {

  public static final int SHORT_PERIODS_NUM = 20;
  public static final int LONG_PERIODS_NUM = 40;
  public static final String SHORT_TERM = "Short Term";
  public static final String LONG_TERM = "Long Term";

  @Override
  public void png(String csv, PngProps props, OutputStream os) throws IOException {
    try (var is = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))) {
      var df = DataFrame.read(is).csv(LocalDate.class, x -> {
        x.setRowKeyColumnName(props.getRowKeyColumnName());
      });

      var columnName = props.getColumnName();
      var close = df.cols()
          .select(column -> column.key().equalsIgnoreCase(columnName));

      var sema = ema(close.copy().cols().replaceKey(columnName, SHORT_TERM),
          smoothConstant(SHORT_PERIODS_NUM),
          SHORT_TERM);

      var lema = ema(close.copy().cols().replaceKey(columnName, LONG_TERM),
          smoothConstant(LONG_PERIODS_NUM),
          LONG_TERM);

      var ema = DataFrame.concatColumns(close, sema, lema);

      Chart.create().asSwing().withLinePlot(ema, chart -> {
        chart.title().withText("Close");
        chart.plot().axes().domain().label().withText("Date");
        chart.plot().axes().range(0).label().withText("Price");
        chart.plot().style(columnName).withLineWidth(1f).withColor(Color.BLUE);
        chart.plot().style(SHORT_TERM).withLineWidth(1f).withColor(Color.GREEN);
        chart.plot().style(LONG_TERM).withLineWidth(1f).withColor(Color.ORANGE);
        chart.legend().on().bottom();
        chart.writerPng(os, props.getWidth(), props.getHeight(), false);
      });
    }
  }

  private static DataFrame<LocalDate, String> ema(DataFrame<LocalDate, String> df, double k, String columnName) {
    AtomicReference<Double> ref = new AtomicReference<>(null);
    df.cols().applyDoubles(columnName, x -> {
      var current = (Double) ((DataFrameCursor) x).col(columnName).getValue();
      if (ref.get() == null) {
        ref.set(current);
        return current;
      } else {
        var prevEma = ref.get();
        var ema = (current - prevEma) * k + prevEma;
        ref.set(ema);
        return ema;
      }
    });
    return df;
  }

  private static double smoothConstant(int n) {
    return 2.0 / (n + 1);
  }
}