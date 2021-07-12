package me.devtools4.telegram.service;

import com.github.mustachejava.Mustache;
import java.io.IOException;
import java.io.StringWriter;

public class MustacheRender {
  private final Mustache m;

  public MustacheRender(Mustache m) {
    this.m = m;
  }

  public <T> String html(T t) {
    try (var writer = new StringWriter()) {
      m.execute(writer, t)
          .flush();
      return writer.toString();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}
