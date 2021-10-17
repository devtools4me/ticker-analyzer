package me.devtools4.telegram.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public enum Command {
  UNKNOWN("unknown"),
  START("/start"),
  QUOTE("/quote") {
    @Override
    public Map<String, String> params(String text) {
      return Ops.params(text, Collections.emptyList(), QUOTE);
    }
  },
  HISTORY("/history") {
    @Override
    public Map<String, String> params(String text) {
      return Ops.params(text, periodPatterns(), HISTORY);
    }

    @Override
    public List<String> periodPatterns() {
      return Ops.periodPatterns(HISTORY);
    }
  },
  SMA("/sma") {
    @Override
    public Map<String, String> params(String text) {
      return Ops.params(text, periodPatterns(), SMA);
    }

    @Override
    public List<String> periodPatterns() {
      return Ops.periodPatterns(SMA);
    }
  },
  EMA("/ema") {
    @Override
    public Map<String, String> params(String text) {
      return Ops.params(text, periodPatterns(), EMA);
    }

    @Override
    public List<String> periodPatterns() {
      return Ops.periodPatterns(EMA);
    }
  },
  BLSH("/blsh") {
    @Override
    public Map<String, String> params(String text) {
      return Ops.params(text, periodPatterns(), BLSH);
    }

    @Override
    public List<String> periodPatterns() {
      return Ops.periodPatterns(BLSH);
    }
  };

  private final String path;

  Command(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public Map<String, String> params(String text) {
    return Collections.emptyMap();
  }

  public Boolean is(Command other) {
    return this == other;
  }

  public Boolean is(String text) {
    return periodPatterns().stream().anyMatch(text::startsWith) ||
        text.startsWith(getPath()) ||
        ("/" + text).startsWith(getPath());
  }

  public List<String> periodPatterns() {
    return Collections.emptyList();
  }

  public static Command of(String text) {
    return Arrays.stream(Command.values())
        .filter(x -> x.is(text))
        .findFirst()
        .orElse(Command.UNKNOWN);
  }
}