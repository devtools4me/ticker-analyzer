package me.devtools4.telegram.df.chart;

import java.awt.Color;
import java.io.OutputStream;
import me.devtools4.telegram.df.DF;
import me.devtools4.telegram.df.PngProps;

public class HistoryChartStrategy implements ChartStrategy {

  @Override
  public void png(String csv, PngProps props, OutputStream os) {
    var columnName = props.getColumnName();
    var close = new DF(csv, props.getRowKeyColumnName())
        .select(columnName);

    close.chartWithLinePlot(chart -> {
      chart.title().withText("Close");
      chart.plot().style("Close").withLineWidth(1f).withColor(Color.BLUE);
      chart.plot().axes().domain().label().withText("Date");
      chart.plot().axes().range(0).label().withText("Price");
      chart.writerPng(os, props.getWidth(), props.getHeight(), false);
    });
  }
}