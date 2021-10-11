package me.devtools4.telegram.api;

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
      return Ops.params(text, Command.historyPatterns, HISTORY);
    }
  },
  SMA("/sma") {
    @Override
    public Map<String, String> params(String text) {
      return Ops.params(text, Command.smaPatterns, SMA);
    }
  },
  BLSH("/blsh") {
    @Override
    public Map<String, String> params(String text) {
      return Ops.params(text, Command.blshPatterns, BLSH);
    }
  };

  private static final List<String> historyPatterns = Ops.periodPatterns(HISTORY);
  private static final List<String> smaPatterns = Ops.periodPatterns(SMA);
  private static final List<String> blshPatterns = Ops.periodPatterns(BLSH);

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

  public static Command of(String text) {
    if (text.startsWith(START.getPath())) {
      return START;
    } else if (text.startsWith(QUOTE.getPath())) {
      return QUOTE;
    } else if (historyPatterns.stream().anyMatch(text::startsWith) || text.startsWith(HISTORY.getPath())) {
      return HISTORY;
    } else if (smaPatterns.stream().anyMatch(text::startsWith) || text.startsWith(SMA.getPath())) {
      return SMA;
    } else if (blshPatterns.stream().anyMatch(text::startsWith) || text.startsWith(BLSH.getPath())) {
      return BLSH;
    } else {
      return UNKNOWN;
    }
  }
}