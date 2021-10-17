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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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
        Arguments.of("/start", checkApiStr("data/start.json")),
        Arguments.of("/quote/msft", checkApiStr("data/quote.json")),
        Arguments.of("/history/msft", checkApi("data/history-1m.png")),
        Arguments.of("/history/1y/msft", checkApi("data/history-1y.png")),
        Arguments.of("/sma/msft", checkApi("data/sma-1m.png")),
        Arguments.of("/sma/1y/msft", checkApi("data/sma-1y.png")),
        Arguments.of("/ema/msft", checkApi("data/ema-1m.png")),
        Arguments.of("/ema/1y/msft", checkApi("data/ema-1y.png")),
        Arguments.of("/blsh/msft", checkApi("data/blsh-1m.png")),
        Arguments.of("/blsh/1y/msft", checkApi("data/blsh-1y.png"))
    );
  }

  private static Consumer<EntityExchangeResult<byte[]>> checkApiStr(String file) {
    return x -> {
      var bytes = x.getResponseBody();
      assertNotNull(bytes);
      assertThat(new String(bytes), is(res2str(file)));
    };
  }

  private static Consumer<EntityExchangeResult<byte[]>> checkApi(String file) {
    return x -> {
      var bytes = x.getResponseBody();
      assertNotNull(bytes);
      assertThat(bytes, is(res2bytes(file)));
    };
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
        Arguments.of(update(1L, "/quote/msft"), checkMessage("1", "data/quote.html")),
        Arguments.of(query(1L, 1,  "/quote"), checkEdit("1", 1, "quote.html")),
        Arguments.of(query(1L, 1,  "/history"), checkEdit("1", 1, "history.html")),
        Arguments.of(query(1L, 1,  "/sma"), checkEdit("1", 1, "sma.html")),
        Arguments.of(query(1L, 1,  "/ema"), checkEdit("1", 1, "ema.html")),
        Arguments.of(query(1L, 1,  "/blsh"), checkEdit("1", 1, "blsh.html")),
        Arguments.of(update(1L, "/history/msft"), checkPhoto("1", "data/history-1m.png")),
        Arguments.of(update(1L, "/history/1y/msft"), checkPhoto("1", "data/history-1y.png")),
        Arguments.of(update(1L, "/sma/msft"), checkPhoto("1", "data/sma-1m.png")),
        Arguments.of(update(1L, "/sma/1y/msft"), checkPhoto("1", "data/sma-1y.png")),
        Arguments.of(update(1L, "/ema/msft"), checkPhoto("1", "data/ema-1m.png")),
        Arguments.of(update(1L, "/ema/1y/msft"), checkPhoto("1", "data/ema-1y.png")),
        Arguments.of(update(1L, "/blsh/msft"), checkPhoto("1", "data/blsh-1m.png")),
        Arguments.of(update(1L, "/blsh/1y/msft"), checkPhoto("1", "data/blsh-1y.png"))
    );
  }

  private static Consumer<TestWebhookBot> checkMessage(String chatId, String file) {
    return x -> {
      SendChatAction a = x.get(0);
      assertNotNull(a);
      assertThat(a.getActionType(), is(ActionType.TYPING));
      SendMessage sm = x.get(1);
      assertThat(sm.getChatId(), is(chatId));
      assertThat(sm.getParseMode(), is("HTML"));
      assertThat(sm.getText(), is(res2str(file)));
    };
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

  private static Consumer<TestWebhookBot> checkEdit(String chatId, Integer messageId, String file) {
    return x -> {
      EditMessageText emt = x.get(0);
      assertNotNull(emt);
      assertThat(emt.getChatId(), is(chatId));
      assertThat(emt.getMessageId(), is(messageId));
      assertThat(emt.getParseMode(), is("HTML"));
      assertThat(emt.getText(), is(res2str(file)));
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

  private static Update query(Long chatId, Integer messageId, String text) {
    var chat = new Chat();
    chat.setId(chatId);
    var message = new Message();
    message.setChat(chat);
    message.setMessageId(messageId);
    var q = new CallbackQuery();
    q.setMessage(message);
    q.setData(text);
    var update = new Update();
    update.setCallbackQuery(q);
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