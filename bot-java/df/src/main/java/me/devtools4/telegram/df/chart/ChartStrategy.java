package me.devtools4.telegram.df.chart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import me.devtools4.telegram.df.PngProps;

public interface ChartStrategy {
  void png(String csv, PngProps props, OutputStream os) throws IOException;

  default byte[] png(String csv, PngProps props) {
    try (var os = new ByteArrayOutputStream()) {
      png(csv, props, os);
      return os.toByteArray();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}