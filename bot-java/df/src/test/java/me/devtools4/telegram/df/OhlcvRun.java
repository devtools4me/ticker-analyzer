package me.devtools4.telegram.df;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;

public class OhlcvRun {
  public static void main(String[] args) throws IOException {
    try (var is = OhlcvRun.class.getClassLoader().getResourceAsStream("GLD.csv");
         var os = new FileOutputStream("test.png", false)) {
      var csv = IOUtils.toString(is, Charset.defaultCharset());
      var ohlcv = new Ohlcv(csv);
      ohlcv.png(os, 500, 500);
      os.flush();
    }
  }
}