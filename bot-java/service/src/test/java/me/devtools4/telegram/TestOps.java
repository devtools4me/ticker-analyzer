package me.devtools4.telegram;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import org.apache.commons.io.IOUtils;

public class TestOps {
  public static void bytes2file(String name, byte[] bytes) {
    try {
      Files.write(Paths.get(name), bytes);
    } catch (IOException e) {
      throw new IllegalArgumentException(name);
    }
  }

  public static byte[] res2bytes(String name) {
    return withResource(name, x -> is2bytes(x));
  }

  public static byte[] is2bytes(InputStream x) {
    try {
      return IOUtils.toByteArray(x);
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public static String res2str(String name) {
    return withResource(name, x -> {
      try {
        return IOUtils.toString(x, Charset.defaultCharset());
      } catch (Exception ex) {
        throw new IllegalArgumentException("read resource error, name=" + name, ex);
      }
    });
  }

  public static <T> T withResource(String name, Function<InputStream, T> func) {
    try (var is = TickerAnalyzerAppTest.class.getClassLoader().getResourceAsStream(name)) {
      return func.apply(is);
    } catch (IOException e) {
      throw new IllegalArgumentException(name);
    }
  }
}