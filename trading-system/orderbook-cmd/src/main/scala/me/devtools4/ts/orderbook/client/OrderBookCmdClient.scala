package me.devtools4.ts.orderbook.client

import me.devtools4.ts.dto.{CommandResponse, OrderBookCommand}

class OrderBookCmdClient(basePath: String) {
  def send(cmd: OrderBookCommand): CommandResponse = {
    val r = requests.post(
      s"$basePath/order",
      data = upickle.default.stream(cmd)
    )
    upickle.default.read[CommandResponse](r.text())
  }
  def update() = ???
  def delete() = ???
}

object OrderBookCmdClient {
  def apply(basePath: String) = new OrderBookCmdClient(basePath)
}