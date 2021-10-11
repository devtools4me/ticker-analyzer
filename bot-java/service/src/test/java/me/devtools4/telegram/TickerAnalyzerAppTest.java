package me.devtools4.telegram;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import com.yahoo.finanance.query1.Query1ApiController;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@SpringBootTest(webEnvironment = DEFINED_PORT)
@Import({
    TickerAnalyzerApp.class
})
@ComponentScan(basePackageClasses = Query1ApiController.class)
public class TickerAnalyzerAppTest {

  @LocalServerPort
  private int port = 0;

  private WebTestClient webClient;

  private static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of("/start", (Consumer<EntityExchangeResult<byte[]>>) x -> {
          var bytes = x.getResponseBody();
          assertNotNull(bytes);
          assertThat(new String(bytes), is(TestOps.res2str("data/start.json")));
        }),
        Arguments.of("/quote/msft", (Consumer<EntityExchangeResult<byte[]>>) x -> {
          var bytes = x.getResponseBody();
          assertNotNull(bytes);
          assertThat(new String(bytes), is(TestOps.res2str("data/quote.json")));
        })
//        Arguments.of("/history/msft"),
//        Arguments.of("/history/1y/msft"),
//        Arguments.of("/sma/msft"),
//        Arguments.of("/sma/1y/msft"),
//        Arguments.of("/blsh/msft"),
//        Arguments.of("/blsh/1y/msft")
    );
  }

  @BeforeEach
  public void before() {
    var baseUri = "http://localhost:" + port;
    log.info("baseUri={}", baseUri);

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