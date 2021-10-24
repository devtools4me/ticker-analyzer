package me.devtools4.telegram.df.chart;

import static me.devtools4.telegram.df.DF.emaSmoothConstant;
import static me.devtools4.telegram.df.chart.Ops.joinImages;

import com.d3x.morpheus.viz.chart.Chart;
import com.d3x.morpheus.viz.chart.xy.XyPlot;
import java.awt.Color;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.function.Consumer;
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
  public void png(String csv, PngProps props, OutputStream os) {
    var columnName = props.getColumnName();
    var close = new DF(csv, props.getRowKeyColumnName())
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
        .ema(MACD_H, emaSmoothConstant(SHORT_PERIODS_NUM))
        .select(MACD_H);

    var all = macd
        .concat(macd_s);

    var newProps = props.withHeight((props.getHeight() + 1) / 2);
    var img1 = withOutputStream(x -> all.chartWithLinePlot(writeMacdSignal(newProps, x)));
    var img2 = withOutputStream(x -> macd_h.chartWithBarPlot(writeMacdHist(newProps, x)));
    joinImages(img1, img2, os);
  }

  private Consumer<Chart<XyPlot<LocalDate>>> writeMacdSignal(PngProps props, OutputStream os) {
    return chart -> {
      chart.title().withText(MACD);
      chart.plot().axes().domain().label().withText("Date");
      chart.plot().axes().range(0).label().withText(MACD);
      chart.plot().style(MACD).withLineWidth(1f).withColor(Color.BLACK);
      chart.plot().style(MACD_S).withLineWidth(1f).withColor(Color.GREEN);
      chart.legend().on().bottom();
      chart.writerPng(os, props.getWidth(), props.getHeight(), false);
    };
  }

  private Consumer<Chart<XyPlot<LocalDate>>> writeMacdHist(PngProps props, OutputStream os) {
    return chart -> {
      chart.title().withText(MACD);
      chart.plot().axes().domain().label().withText("Date");
      chart.plot().axes().range(0).label().withText(MACD);
      chart.plot().style(MACD_H).withLineWidth(1f).withColor(Color.GRAY);
      chart.legend().on().bottom();
      chart.writerPng(os, props.getWidth(), props.getHeight(), false);
    };
  }
}