package me.devtools4.telegram.df.chart;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class JoinImagesRun {

  public static void main(String[] args) throws Exception {
    try (var is1 = new FileInputStream("test-1.png");
        var is2 = new FileInputStream("test-2.png");
        var out = new FileOutputStream("merge.png")) {
      Ops.joinImages(is1, is2, out);
    }
  }
}