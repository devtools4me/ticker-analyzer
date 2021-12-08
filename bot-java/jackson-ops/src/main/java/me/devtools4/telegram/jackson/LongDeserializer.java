package me.devtools4.telegram.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class LongDeserializer extends JsonDeserializer<Long> {

  @Override
  public Long deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    try {
      return Long.parseLong(p.getText());
    } catch (NumberFormatException e) {
      return null;
    }
  }
}