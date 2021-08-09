package me.devtools4.telegram.service;

import com.github.mustachejava.Mustache;
import java.io.IOException;
import java.io.StringWriter;

public class MustacheRender {
  private final Mustache m;
  private final Mustache error;

  public MustacheRender(Mustache m, Mustache error) {
    this.m = m;
    this.error = error;
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

  public String error(Throwable t) {
    try (var writer = new StringWriter()) {
      error.execute(writer, t)
          .flush();
      return writer.toString();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}
