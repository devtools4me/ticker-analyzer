package me.devtools4.telegram.service;

import me.devtools4.telegram.api.Period;
import org.springframework.core.convert.converter.Converter;

public class StringToPeriodConverter implements Converter<String, Period> {

  @Override
  public Period convert(String s) {
    return Period.convert(s);
  }
}