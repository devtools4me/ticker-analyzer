package me.devtools4.telegram.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.util.UriTemplate;

public enum Command {
  UNKNOWN("unknown"),
  START("/start"),
  QUOTE("/quote"),
  HISTORY("/history"),
  SMA("/sma"),
  EMA("/ema"),
  APO("/apo"),
  MACD("/macd"),
  MOM("/mom"),
  BLSH("/blsh");

  private final String path;

  Command(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public Boolean is(Command other) {
    return this == other;
  }

  public static Command of(String text) {
    Map<String, String> map = params(text);
    return Optional.ofNullable(map.get("cmd"))
        .map(String::toUpperCase)
        .filter(names::contains)
        .map(Command::valueOf)
        .orElse(Command.UNKNOWN);
  }

  public static Map<String, String> params(String text) {
    return templates.stream()
        .map(x -> x.match(text))
        .filter(x -> !x.isEmpty())
        .findFirst()
        .orElseGet(Map::of);
  }

  private static final Set<String> names = Arrays.stream(Command.values())
      .map(Command::name)
      .collect(Collectors.toSet());

  private static final List<UriTemplate> templates = List.of(
      new UriTemplate("/{cmd}/{period}/{id}?i={indicator}"),
      new UriTemplate("/{cmd}/{period}/{id}"),
      new UriTemplate("/{cmd}/{id}?i={indicator}"),
      new UriTemplate("/{cmd}/{id}"),
      new UriTemplate("/{cmd}")
  );
}