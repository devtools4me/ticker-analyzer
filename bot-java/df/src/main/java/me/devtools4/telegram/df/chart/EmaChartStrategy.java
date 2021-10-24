package me.devtools4.telegram.df.chart;

import static me.devtools4.telegram.df.DF.emaSmoothConstant;

import java.awt.Color;
import java.io.OutputStream;
import me.devtools4.telegram.df.DF;
import me.devtools4.telegram.df.PngProps;

public class EmaChartStrategy implements ChartStrategy {

  public static final int SHORT_PERIODS_NUM = 20;
  public static final int LONG_PERIODS_NUM = 40;
  public static final String SHORT_TERM = "Short Term";
  public static final String LONG_TERM = "Long Term";

  @Override
  public void png(String csv, PngProps props, OutputStream os) {
    var columnName = props.getColumnName();
    var close = new DF(csv, props.getRowKeyColumnName())
        .select(columnName);

    var sema = close.replaceKey(columnName, SHORT_TERM)
        .ema(SHORT_TERM, emaSmoothConstant(SHORT_PERIODS_NUM));

    var lema = close.replaceKey(columnName, LONG_TERM)
        .ema(LONG_TERM, emaSmoothConstant(LONG_PERIODS_NUM));

    var ema = close.concat(sema, lema);

    ema.chartWithLinePlot(chart -> {
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