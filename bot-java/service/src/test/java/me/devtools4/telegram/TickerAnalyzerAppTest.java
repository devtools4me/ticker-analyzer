package me.devtools4.telegram;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TickerAnalyzerApp.class)
public class TickerAnalyzerAppTest {

  @LocalServerPort
  private int port = 0;

  private WebTestClient webClient;

  private static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of("/start", (Consumer<EntityExchangeResult<byte[]>>) x -> {
          var bytes = x.getResponseBody();
          assertNotNull(bytes);
          assertThat(new String(bytes), is(res2str("data/start.json")));
        })
//        Arguments.of("/quote/msft"),
//        Arguments.of("/history/msft"),
//        Arguments.of("/history/1y/msft"),
//        Arguments.of("/sma/msft"),
//        Arguments.of("/sma/1y/msft"),
//        Arguments.of("/blsh/msft"),
//        Arguments.of("/blsh/1y/msft")
    );
  }

  private static String res2str(String name) {
    try (var is = TickerAnalyzerAppTest.class.getClassLoader().getResourceAsStream(name)) {
      return IOUtils.toString(is, Charset.defaultCharset());
    } catch (IOException e) {
      throw new IllegalArgumentException(name);
    }
  }

  @BeforeEach
  public void before() {
    var baseUri = "http://localhost:" + port;
    webClient = WebTestClient.bindToServer()
        .responseTimeout(Duration.ofSeconds(10))
        .baseUrl(baseUri)
        .build();
  }

  @ParameterizedTest
  @MethodSource("arguments")
  public void test(String path, Consumer<EntityExchangeResult<byte[]>> func) {
    webClient
        .get().uri(path)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(func);
  }
}