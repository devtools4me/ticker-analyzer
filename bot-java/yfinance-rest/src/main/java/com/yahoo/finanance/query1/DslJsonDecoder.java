package com.yahoo.finanance.query1;

import com.dslplatform.json.DslJson;
import feign.Response;
import feign.codec.Decoder;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import org.apache.commons.io.IOUtils;

public class DslJsonDecoder implements Decoder {

  private final DslJson json;

  public DslJsonDecoder() {
    this(new DslJson<>());
  }

  public DslJsonDecoder(DslJson json) {
    this.json = json;
  }

  @Override
  public Object decode(Response response, Type type) throws IOException {
    if (response.body() == null)
      return null;

    try (InputStream reader = response.body().asInputStream()) {
      return json.deserialize(type, reader);
    }
  }
}