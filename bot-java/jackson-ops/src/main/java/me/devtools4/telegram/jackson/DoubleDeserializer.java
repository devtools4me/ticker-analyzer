package me.devtools4.telegram.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class DoubleDeserializer extends JsonDeserializer<Double> {

  @Override
  public Double deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    try {
      return Double.parseDouble(p.getText());
    } catch (NumberFormatException e) {
      return null;
    }
  }
}