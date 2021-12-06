package co.alphavantage;

import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;

public class AlphaVantageExceptionDecoder implements ErrorDecoder {

  final Decoder decoder;
  final ErrorDecoder defaultDecoder = new Default();

  public AlphaVantageExceptionDecoder(Decoder decoder) {
    this.decoder = decoder;
  }

  @Override
  public Exception decode(String methodKey, Response response) {
    AlphaVantageException error = new AlphaVantageException();
    error.setMessage(defaultDecoder.decode(methodKey, response).getMessage());
    return error;
  }
}