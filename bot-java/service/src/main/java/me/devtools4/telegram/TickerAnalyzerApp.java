package me.devtools4.telegram;

import me.devtools4.telegram.config.TickerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
    TickerConfig.class
})
public class TickerAnalyzerApp {

  public static void main(String[] args) {
    SpringApplication.run(TickerAnalyzerApp.class, args);
  }
}