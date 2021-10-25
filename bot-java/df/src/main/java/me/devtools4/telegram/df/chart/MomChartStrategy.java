package me.devtools4.telegram.df.chart;

import java.awt.Color;
import java.io.OutputStream;
import me.devtools4.telegram.df.DF;
import me.devtools4.telegram.df.PngProps;

public class MomChartStrategy implements ChartStrategy {
  public static final String MOM = "Momentum";
  public static final int TIME_OFFSET = 20;

  @Override
  public void png(String csv, PngProps props, OutputStream os) {
    var columnName = props.getColumnName();
    var close = new DF(csv, props.getRowKeyColumnName())
        .select(columnName);

    var mom = close
        .replaceKey(columnName, MOM)
        .momentum(MOM, TIME_OFFSET);

    mom.chartWithLinePlot(chart -> {
      chart.title().withText(MOM);
      chart.plot().axes().domain().label().withText("Date");
      chart.plot().axes().range(0).label().withText(MOM);
      chart.plot().style(MOM).withLineWidth(1f).withColor(Color.BLUE);
      chart.legend().on().bottom();
      chart.writerPng(os, props.getWidth(), props.getHeight(), false);
    });
  }
}