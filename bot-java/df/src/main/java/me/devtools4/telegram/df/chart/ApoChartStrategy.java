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

public class ApoChartStrategy implements ChartStrategy {

  public static final int SHORT_PERIODS_NUM = 20;
  public static final int LONG_PERIODS_NUM = 40;
  public static final String SHORT_TERM = "Short Term";
  public static final String LONG_TERM = "Long Term";
  public static final String APO = "APO";

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

      var apo = apo(ema.copy(), SHORT_TERM, LONG_TERM)
          .cols()
          .select(column -> column.key().equalsIgnoreCase(LONG_TERM))
          .copy()
          .cols()
          .replaceKey(LONG_TERM, APO);

      Chart.create().asSwing().withLinePlot(apo, chart -> {
        chart.title().withText(APO);
        chart.plot().axes().domain().label().withText("Date");
        chart.plot().axes().range(0).label().withText(APO);
        chart.plot().style(APO).withLineWidth(1f).withColor(Color.BLUE);
        chart.legend().on().bottom();
        chart.writerPng(os, props.getWidth(), props.getHeight(), false);
      });
    }
  }

  private static DataFrame<LocalDate, String> ema(DataFrame<LocalDate, String> df, double k, String columnName) {
    AtomicReference<Double> ref = new AtomicReference<>(null);
    return df.cols().applyDoubles(columnName, x -> {
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
  }

  private static DataFrame<LocalDate, String> apo(DataFrame<LocalDate, String> df, String fastColumnName, String slowColumnName) {
    var ret = df.cols().applyDoubles(fastColumnName, x -> {
      var fast = (Double) ((DataFrameCursor) x).col(fastColumnName).getValue();
      var slow = (Double) ((DataFrameCursor) x).col(slowColumnName).getValue();
      return fast - slow;
    });
    return ret;
  }

  private static double smoothConstant(int n) {
    return 2.0 / (n + 1);
  }
}