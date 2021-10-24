package me.devtools4.telegram.df.chart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;
import me.devtools4.telegram.df.PngProps;

public interface ChartStrategy {
  void png(String csv, PngProps props, OutputStream os);

  default byte[] png(String csv, PngProps props) {
    return withOutputStream(os -> png(csv, props, os));
  }

  default byte[] withOutputStream(Consumer<OutputStream> consumer) {
    try (var os = new ByteArrayOutputStream()) {
      consumer.accept(os);
      return os.toByteArray();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}