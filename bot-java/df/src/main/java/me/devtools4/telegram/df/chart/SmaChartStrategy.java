package me.devtools4.telegram.df.chart;

import java.awt.Color;
import java.io.OutputStream;
import me.devtools4.telegram.df.DF;
import me.devtools4.telegram.df.PngProps;

public class SmaChartStrategy implements ChartStrategy {

  @Override
  public void png(String csv, PngProps props, OutputStream os) {
    var columnName = props.getColumnName();
    var close = new DF(csv, props.getRowKeyColumnName())
        .select(columnName);

    var st = close
        .mean(40)
        .replaceKey(columnName, "Short Term");

    var lt = close
        .mean(70)
        .replaceKey(columnName, "Long Term");

    var sam = close.concat(st, lt);

    sam.chartWithLinePlot(chart -> {
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