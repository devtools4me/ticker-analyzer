package me.devtools4.telegram.service;

import java.io.IOException;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class YahooFinanceRun {

  public static void main(String[] args) throws IOException {
    Stock stock = YahooFinance.get("INTC");
    stock.print();
  }
}