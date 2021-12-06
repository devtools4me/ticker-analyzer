package feign.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class JacksonDecoder implements Decoder {

  private final ObjectMapper mapper;

  public JacksonDecoder(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public Object decode(Response response, Type type) throws IOException {
    if (response.body() == null) {
      return null;
    }

    try (InputStream reader = response.body().asInputStream()) {
      return mapper.readValue(reader, (Class<?>)type);
    }
  }
}