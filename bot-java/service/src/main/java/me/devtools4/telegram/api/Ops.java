package me.devtools4.telegram.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;

public class Ops {

  public static Map<String, String> params(String text, List<String> patterns, Command command) {
    return patterns.stream()
        .filter(text::startsWith)
        .findFirst()
        .map(x -> {
          var period = Period.convert(x.replace(command.getPath() + "/", ""));
          var id = text.replace(x + "/", "");
          var map = parseIdAndParams(id);
          map.put("period", period.getPeriod());
          return map;
        })
        .orElseGet(() -> {
          var id = text.replace(command.getPath() + "/", "");
          return parseIdAndParams(id);
        });
  }

  private static Map<String, String> parseIdAndParams(String text) {
    var map = new HashMap<String, String>();
    map.put("id", text);
    return map;
  }

  public static Map<String, String> params(String text) {
    return Arrays.stream(text.split(","))
        .map(x -> {
          var arr = x.split("=");
          if (arr.length != 2) {
            throw new IllegalArgumentException("text=" + text + ", x=" + x);
          }
          return Pair.of(arr[0], arr[1]);
        })
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  }

  public static List<String> periodPatterns(Command cmd) {
    return Arrays.stream(Period.values())
        .map(Period::getPeriod)
        .map(x -> cmd.getPath() + "/" + x)
        .collect(Collectors.toList());
  }

  public static BinaryOperator<String> reduce(String delimiter) {
    return (s1, s2) -> s1.isEmpty() ? s2 : s1 + delimiter + s2;
  }
}