package me.devtools4.telegram.df.chart;

import static me.devtools4.telegram.df.chart.Ops.joinImages;

import java.io.IOException;
import java.io.OutputStream;
import me.devtools4.telegram.df.PngProps;

public class JoinChartStrategy implements ChartStrategy {
  private final ChartStrategy s1;
  private final ChartStrategy s2;

  public JoinChartStrategy(ChartStrategy s1, ChartStrategy s2) {
    this.s1 = s1;
    this.s2 = s2;
  }

  @Override
  public void png(String csv, PngProps props, OutputStream os) throws IOException {
    var newProps = props.withHeight((props.getHeight() + 1) / 2);
    joinImages(s1.png(csv, newProps), s2.png(csv, newProps), os);
  }
}