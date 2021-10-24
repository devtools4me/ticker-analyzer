package me.devtools4.telegram.df.chart;

import com.d3x.morpheus.frame.DataFrame;
import com.d3x.morpheus.viz.chart.Chart;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import me.devtools4.telegram.df.PngProps;

public class HistoryChartStrategy implements ChartStrategy {

  @Override
  public void png(String csv, PngProps props, OutputStream os) {
    try (var is = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))) {
      var df = DataFrame.read(is).csv(LocalDate.class, x -> {
        x.setRowKeyColumnName(props.getRowKeyColumnName());
      });

      var columnName = props.getColumnName();
      var close = df.cols()
          .select(column -> column.key().equalsIgnoreCase(columnName));

      Chart.create().asSwing().withLinePlot(close, chart -> {
        chart.title().withText("Close");
        chart.plot().style("Close").withLineWidth(1f).withColor(Color.BLUE);
        chart.plot().axes().domain().label().withText("Date");
        chart.plot().axes().range(0).label().withText("Price");
        chart.writerPng(os, props.getWidth(), props.getHeight(), false);
      });
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}