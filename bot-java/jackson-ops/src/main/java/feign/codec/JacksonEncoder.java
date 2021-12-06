package feign.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestTemplate;
import java.io.IOException;
import java.lang.reflect.Type;

public class JacksonEncoder implements Encoder {

  private final ObjectMapper mapper;

  public JacksonEncoder(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void encode(Object object, Type bodyType, RequestTemplate template) {
    try {
      template.body(mapper.writeValueAsString(object));
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}