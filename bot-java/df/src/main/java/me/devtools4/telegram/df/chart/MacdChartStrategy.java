package me.devtools4.telegram.df.chart;

import static me.devtools4.telegram.df.DF.emaSmoothConstant;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import me.devtools4.telegram.df.DF;
import me.devtools4.telegram.df.PngProps;

public class MacdChartStrategy implements ChartStrategy {

  public static final int SHORT_PERIODS_NUM = 20;
  public static final int LONG_PERIODS_NUM = 40;
  public static final String SHORT_TERM = "Short Term";
  public static final String LONG_TERM = "Long Term";
  public static final String MACD = "MACD";
  public static final String MACD_S = "MACD Signal";
  public static final String MACD_H = "MACD Histogram";

  @Override
  public void png(String csv, PngProps props, OutputStream os) throws IOException {
    try (var is = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))) {
      var columnName = props.getColumnName();
      var close = new DF(is, props.getRowKeyColumnName())
          .select(columnName);

      var sema = close
          .replaceKey(columnName, SHORT_TERM)
          .ema(SHORT_TERM, emaSmoothConstant(SHORT_PERIODS_NUM));

      var lema = close
          .replaceKey(columnName, LONG_TERM)
          .ema(LONG_TERM, emaSmoothConstant(LONG_PERIODS_NUM));

      var ema = close.concat(sema, lema);

      var macd = ema.subtract(SHORT_TERM, LONG_TERM)
          .select(LONG_TERM)
          .replaceKey(LONG_TERM, MACD);

      var macd_s = macd
          .replaceKey(MACD, MACD_S)
          .ema(MACD_S, emaSmoothConstant(SHORT_PERIODS_NUM));

      var macd_macd_s = macd.concat(macd_s);

      var macd_h = macd_macd_s
          .replaceKey(MACD, MACD_H)
          .ema(MACD_H, emaSmoothConstant(SHORT_PERIODS_NUM));

      var all = macd
          .concat(macd_s);

      all.chart(chart -> {
        chart.title().withText(MACD);
        chart.plot().axes().domain().label().withText("Date");
        chart.plot().axes().range(0).label().withText(MACD);
        chart.plot().style(MACD).withLineWidth(1f).withColor(Color.BLACK);
        chart.plot().style(MACD_S).withLineWidth(1f).withColor(Color.GREEN);
        chart.legend().on().bottom();
        chart.writerPng(os, props.getWidth(), props.getHeight(), false);
      });
    }
  }
}