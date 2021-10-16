package me.devtools4.telegram;

import static me.devtools4.telegram.TestOps.is2bytes;
import static me.devtools4.telegram.TestOps.res2bytes;
import static me.devtools4.telegram.TestOps.res2str;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import com.yahoo.finanance.query1.Query1ApiController;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import me.devtools4.telegram.TickerAnalyzerAppTest.TestConfig;
import me.devtools4.telegram.service.CommandHandler;
import me.devtools4.telegram.service.TestWebhookBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@SpringBootTest(webEnvironment = DEFINED_PORT)
@Import({
    TickerAnalyzerApp.class,
    TestConfig.class
})
@ComponentScan(basePackageClasses = Query1ApiController.class)
public class TickerAnalyzerAppTest {

  @LocalServerPort
  private int port = 0;

  private WebTestClient webClient;

  @Autowired
  private TestWebhookBot testWebhookBot;

  private static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of("/start", (Consumer<EntityExchangeResult<byte[]>>) x -> {
          var bytes = x.getResponseBody();
          assertNotNull(bytes);
          assertThat(new String(bytes), is(res2str("data/start.json")));
        }),
        Arguments.of("/quote/msft", (Consumer<EntityExchangeResult<byte[]>>) x -> {
          var bytes = x.getResponseBody();
          assertNotNull(bytes);
          assertThat(new String(bytes), is(res2str("data/quote.json")));
        }),
        Arguments.of("/history/msft", (Consumer<EntityExchangeResult<byte[]>>) x -> {
          var bytes = x.getResponseBody();
          assertNotNull(bytes);
          assertThat(bytes, is(TestOps.res2bytes("data/history-1m.png")));
        }),
        Arguments.of("/history/1y/msft", (Consumer<EntityExchangeResult<byte[]>>) x -> {
          var bytes = x.getResponseBody();
          assertNotNull(bytes);
          assertThat(bytes, is(TestOps.res2bytes("data/history-1y.png")));
        }),
        Arguments.of("/sma/msft", (Consumer<EntityExchangeResult<byte[]>>) x -> {
          var bytes = x.getResponseBody();
          assertNotNull(bytes);
          assertThat(bytes, is(TestOps.res2bytes("data/sma-1m.png")));
        }),
        Arguments.of("/sma/1y/msft", (Consumer<EntityExchangeResult<byte[]>>) x -> {
          var bytes = x.getResponseBody();
          assertNotNull(bytes);
          assertThat(bytes, is(TestOps.res2bytes("data/sma-1y.png")));
        }),
        Arguments.of("/blsh/msft", (Consumer<EntityExchangeResult<byte[]>>) x -> {
          var bytes = x.getResponseBody();
          assertNotNull(bytes);
          assertThat(bytes, is(TestOps.res2bytes("data/blsh-1m.png")));
        }),
        Arguments.of("/blsh/1y/msft", (Consumer<EntityExchangeResult<byte[]>>) x -> {
          var bytes = x.getResponseBody();
          assertNotNull(bytes);
          assertThat(bytes, is(TestOps.res2bytes("data/blsh-1y.png")));
        })
    );
  }

  private static Stream<Arguments> webhookArguments() {
    return Stream.of(
        Arguments.of(update(1L, "/start"), (Consumer<TestWebhookBot>) x -> {
          SendChatAction a = x.get(0);
          assertNotNull(a);
          assertThat(a.getActionType(), is(ActionType.TYPING));
          SendMessage sm = x.get(1);
          assertThat(sm.getChatId(), is("1"));
          assertThat(sm.getText(), is("What would you like to receive?"));
        }),
        Arguments.of(update(1L, "/quote/msft"), (Consumer<TestWebhookBot>) x -> {
          SendChatAction a = x.get(0);
          assertNotNull(a);
          assertThat(a.getActionType(), is(ActionType.TYPING));
          SendMessage sm = x.get(1);
          assertThat(sm.getChatId(), is("1"));
          assertThat(sm.getParseMode(), is("HTML"));
          assertThat(sm.getText(), is(res2str("data/quote.html")));
        }),
        Arguments.of(update(1L, "/history/msft"), checkPhoto("1", "data/history-1m.png")),
        Arguments.of(update(1L, "/history/1y/msft"), checkPhoto("1", "data/history-1y.png")),
        Arguments.of(update(1L, "/sma/msft"), checkPhoto("1", "data/sma-1m.png")),
        Arguments.of(update(1L, "/sma/1y/msft"), checkPhoto("1", "data/sma-1y.png")),
        Arguments.of(update(1L, "/blsh/msft"), checkPhoto("1", "data/blsh-1m.png")),
        Arguments.of(update(1L, "/blsh/1y/msft"), checkPhoto("1", "data/blsh-1y.png"))
    );
  }

  private static Consumer<TestWebhookBot> checkPhoto(String chatId, String file) {
    return x -> {
      SendChatAction a = x.get(0);
      assertNotNull(a);
      assertThat(a.getActionType(), is(ActionType.TYPING));
      SendPhoto sm = x.get(1);
      assertThat(sm.getChatId(), is(chatId));
      assertThat(is2bytes(sm.getPhoto().getNewMediaStream()), is(res2bytes(file)));
    };
  }

  private static Update update(Long chatId, String text) {
    var chat = new Chat();
    chat.setId(chatId);
    var message = new Message();
    message.setChat(chat);
    message.setText(text);
    var update = new Update();
    update.setMessage(message);
    return update;
  }

  @BeforeEach
  public void before() {
    var baseUri = "http://localhost:" + port;
    log.info("baseUri={}", baseUri);

    webClient = WebTestClient.bindToServer()
        .responseTimeout(Duration.ofSeconds(10))
        .baseUrl(baseUri)
        .build();

    testWebhookBot.clear();
  }

  @ParameterizedTest
  @MethodSource("arguments")
  public void testApi(String path, Consumer<EntityExchangeResult<byte[]>> func) {
    webClient
        .get().uri(path)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(func);
  }

  @ParameterizedTest
  @MethodSource("webhookArguments")
  public void testWebhook(Update update, Consumer<TestWebhookBot> func) {
    webClient
        .post().uri("/callback/webhook")
        .body(BodyInserters.fromValue(update))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .consumeWith(x -> func.accept(testWebhookBot));
  }

  public static class TestConfig {

    @Bean
    public TestWebhookBot TestWebhookBot(CommandHandler commandHandler) {
      return new TestWebhookBot(commandHandler);
    }
  }
}