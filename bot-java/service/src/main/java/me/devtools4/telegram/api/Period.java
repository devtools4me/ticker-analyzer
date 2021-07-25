package me.devtools4.telegram.api;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public enum Period {
  OneMonth("1m"),
  ThreeMonths("3m"),
  SixMonths("6m"),
  OneYear("1y"),
  FiveYears("5y"),
  TenYears("10y"),
  TwentyYears("20y");

  private final String value;

  Period(String value) {
    this.value = value;
  }

  public static Period convert(String s) {
    return Arrays.stream(Period.values())
        .filter(x -> x.getValue().equals(s))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unsupported value=[" + s + "]"));
  }

  public String getValue() {
    return value;
  }

  public List<LocalDateTime> times() {
    return List.of(startTime(), LocalDateTime.now());
  }

  private LocalDateTime startTime() {
    switch (this) {
      case OneMonth:
        return LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
      case ThreeMonths:
        return LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
      case SixMonths:
        return LocalDateTime.now().minus(6, ChronoUnit.MONTHS);
      case OneYear:
        return LocalDateTime.now().minus(1, ChronoUnit.YEARS);
      case FiveYears:
        return LocalDateTime.now().minus(5, ChronoUnit.YEARS);
      case TenYears:
        return LocalDateTime.now().minus(10, ChronoUnit.YEARS);
      case TwentyYears:
        return LocalDateTime.now().minus(20, ChronoUnit.YEARS);
      default:
        throw new IllegalArgumentException("Unsupported " + value);
    }
  }
}