package me.devtools4.telegram.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CommandTest {

  @ParameterizedTest
  @CsvSource(value = {
      "/start:/start:",
      "/quote/msft:/quote:id=msft",
      "/history/msft:/history:id=msft",
      "/history/1y/msft:/history:id=msft,period=1y",
      "/sma/msft:/sma:id=msft",
      "/sma/1y/msft:/sma:id=msft,period=1y",
      "/blsh/msft:/blsh:id=msft",
      "/blsh/1y/msft:/blsh:id=msft,period=1y",
      "/error:unknown:"
  }, delimiter = ':')
  public void testCommand(String text, String expected, String params) {
    var cmd = Command.of(text);
    assertThat(cmd.getPath(), is(expected));

    Optional.ofNullable(params)
        .filter(StringUtils::isNotBlank)
        .ifPresent(x -> {
          assertThat(cmd.params(text), is(Ops.params(params)));
        });
  }
}