package com.yahoo.finanance.query1;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class YFinanceException extends RuntimeException {

  private String message;
}