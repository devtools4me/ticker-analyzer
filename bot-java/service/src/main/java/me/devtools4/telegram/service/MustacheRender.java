package me.devtools4.telegram.service;

import com.github.mustachejava.Mustache;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class MustacheRender {

  private final Map<TemplateType, Mustache> mustaches;

  public MustacheRender(Map<TemplateType, Mustache> mustaches) {
    this.mustaches = mustaches;
  }

  public <T> String html(T t, TemplateType type) {
    try (var writer = new StringWriter()) {
      mustaches.get(type)
          .execute(writer, t)
          .flush();
      return writer.toString();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public enum TemplateType {
    Quote, Mul, Error
  }
}
