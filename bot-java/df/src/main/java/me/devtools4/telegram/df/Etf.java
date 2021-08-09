package me.devtools4.telegram.df;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Etf {

  private String symbol;
  private LocalDate startDate;
  private String name;
  private String segment;
  private String issuer;
  private String expenseRatio;
  private String aum;
}