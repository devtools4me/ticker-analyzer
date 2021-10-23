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
      "/quote/msft:/quote:cmd=quote,id=msft",
      "/history/msft:/history:cmd=history,id=msft",
      "/history/1y/msft:/history:cmd=history,id=msft,period=1y",
      "/sma/msft:/sma:cmd=sma,id=msft",
      "/sma/1y/msft:/sma:cmd=sma,id=msft,period=1y",
      "/ema/msft:/ema:cmd=ema,id=msft",
      "/ema/1y/msft:/ema:cmd=ema,id=msft,period=1y",
      "/apo/msft:/apo:cmd=apo,id=msft",
      "/apo/1y/msft:/apo:cmd=apo,id=msft,period=1y",
      "/ema/msft?i=APO:/ema:cmd=ema,id=msft,indicator=APO",
      "/blsh/msft:/blsh:cmd=blsh,id=msft",
      "/blsh/1y/msft:/blsh:cmd=blsh,id=msft,period=1y",
      "/error:unknown:"
  }, delimiter = ':')
  public void testCommand(String text, String expected, String params) {
    var cmd = Command.of(text);
    assertThat(cmd.getPath(), is(expected));

    Optional.ofNullable(params)
        .filter(StringUtils::isNotBlank)
        .ifPresent(x -> {
          assertThat(Command.params(text), is(Ops.params(params)));
        });
  }
}