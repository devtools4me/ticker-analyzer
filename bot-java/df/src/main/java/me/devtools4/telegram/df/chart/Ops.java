package me.devtools4.telegram.df.chart;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

public class Ops {
  private Ops() {}

  public static void joinImages(byte[] bytes1, byte[] bytes2, OutputStream out) {
    try (var is1 = new ByteArrayInputStream(bytes1);
        var is2 = new ByteArrayInputStream(bytes2))
    {
      joinImages(is1, is2, out);
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public static void joinImages(InputStream is1, InputStream is2, OutputStream out) throws IOException {
    var img1 = ImageIO.read(is1);
    var img2 = ImageIO.read(is2);

    var w = img1.getWidth();
    var h = img1.getHeight() + img2.getHeight();
    BufferedImage joined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    Graphics g = joined.getGraphics();
    g.drawImage(img1, 0, 0, null);
    g.drawImage(img2, 0, img1.getHeight(), null);

    ImageIO.write(joined, "PNG", out);
  }
}