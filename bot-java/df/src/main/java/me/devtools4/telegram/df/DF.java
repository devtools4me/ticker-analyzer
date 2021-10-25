package me.devtools4.telegram.df;

import com.d3x.morpheus.frame.DataFrame;
import com.d3x.morpheus.frame.DataFrameCursor;
import com.d3x.morpheus.viz.chart.Chart;
import com.d3x.morpheus.viz.chart.xy.XyPlot;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DF {

  private final DataFrame<LocalDate, String> df;

  public DF(DataFrame<LocalDate, String> df) {
    this.df = df;
  }

  public DF(InputStream is, String col) {
    this(DataFrame
        .read(is)
        .csv(LocalDate.class, x -> x.setRowKeyColumnName(col)));
  }

  public DF(String csv, String col) {
    this(csv2df(csv, col));
  }

  public DF select(String col) {
    return new DF(df
        .cols()
        .select(x -> x.key().equalsIgnoreCase(col)));
  }

  public DF replaceKey(String col1, String col2) {
    return new DF(df
        .copy()
        .cols()
        .replaceKey(col1, col2));
  }

  public DF mean(int windowSize) {
    return new DF(df
        .cols()
        .stats()
        .rolling(windowSize)
        .mean());
  }

  public DF concat(DF... dfs) {
    var res = DataFrame.concatColumns(
        Stream.concat(
            Stream.of(df),
            Arrays.stream(dfs).map(x -> x.df)
        ));
    return new DF(res);
  }

  public DF subtract(String col1, String col2) {
    var res = df.cols().applyDoubles(col1, x -> {
      var fast = (Double) ((DataFrameCursor) x).col(col1).getValue();
      var slow = (Double) ((DataFrameCursor) x).col(col2).getValue();
      return fast - slow;
    });
    return new DF(res);
  }

  public DF ema(String col, double k) {
    AtomicReference<Double> ref = new AtomicReference<>(null);
    var res = df.cols().applyDoubles(col, x -> {
      var current = (Double) ((DataFrameCursor) x).col(col).getValue();
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
    return new DF(res);
  }

  public DF momentum(String col, int offset) {
    var ref = new RingBuffer<Double>(offset);
    var res = df.cols().applyDoubles(col, x -> {
      var current = (Double) ((DataFrameCursor) x).col(col).getValue();
      if (!ref.isFull()) {
        ref.offer(current);
        return 0.0;
      } else {
        var prev = ref.poll();
        ref.offer(current);
        return current - prev;
      }
    });
    return new DF(res);
  }

  public void chartWithLinePlot(Consumer<Chart<XyPlot<LocalDate>>> consumer) {
    Chart.create().asSwing().withLinePlot(df, consumer);
  }

  public void chartWithBarPlot(Consumer<Chart<XyPlot<LocalDate>>> consumer) {
    Chart.create().asSwing().withBarPlot(df, true, consumer);
  }

  public static double emaSmoothConstant(int n) {
    return 2.0 / (n + 1);
  }

  private static DataFrame<LocalDate, String> csv2df(String csv, String col) {
    try (var is = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))) {
      return DataFrame
          .read(is)
          .csv(LocalDate.class, x -> x.setRowKeyColumnName(col));
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}