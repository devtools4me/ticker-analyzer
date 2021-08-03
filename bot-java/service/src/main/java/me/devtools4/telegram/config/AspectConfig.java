package me.devtools4.telegram.config;

import me.devtools4.aops.aspects.TraceAspect;
import org.springframework.context.annotation.Bean;

public class AspectConfig {

  @Bean
  public TraceAspect traceAspect() {
    return new TraceAspect();
  }
}