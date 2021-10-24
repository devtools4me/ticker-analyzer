package me.devtools4.telegram.df.chart;

import static me.devtools4.telegram.df.DF.emaSmoothConstant;

import java.awt.Color;
import java.io.OutputStream;
import me.devtools4.telegram.df.DF;
import me.devtools4.telegram.df.PngProps;

public class ApoChartStrategy implements ChartStrategy {

  public static final int SHORT_PERIODS_NUM = 20;
  public static final int LONG_PERIODS_NUM = 40;
  public static final String SHORT_TERM = "Short Term";
  public static final String LONG_TERM = "Long Term";
  public static final String APO = "APO";

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

    var apo = ema.subtract(SHORT_TERM, LONG_TERM)
        .select(LONG_TERM)
        .replaceKey(LONG_TERM, APO);

    apo.chartWithLinePlot(chart -> {
      chart.title().withText(APO);
      chart.plot().axes().domain().label().withText("Date");
      chart.plot().axes().range(0).label().withText(APO);
      chart.plot().style(APO).withLineWidth(1f).withColor(Color.BLUE);
      chart.legend().on().bottom();
      chart.writerPng(os, props.getWidth(), props.getHeight(), false);
    });
  }

}