package com.yahoo.finanance.query1;

import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;

public class YFinanceExceptionDecoder implements ErrorDecoder {

  final Decoder decoder;
  final ErrorDecoder defaultDecoder = new Default();

  public YFinanceExceptionDecoder(Decoder decoder) {
    this.decoder = decoder;
  }

  @Override
  public Exception decode(String methodKey, Response response) {
    YFinanceException error = new YFinanceException();
    error.setMessage(defaultDecoder.decode(methodKey, response).getMessage());
    return error;
  }
}