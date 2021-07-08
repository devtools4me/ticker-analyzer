package me.devtools4.telegram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TickerController {
  @GetMapping("/check")
  public String check() {
    return "Application is alive";
  }
}
