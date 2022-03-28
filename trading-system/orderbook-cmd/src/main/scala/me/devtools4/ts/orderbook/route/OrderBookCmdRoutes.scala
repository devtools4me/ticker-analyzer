package me.devtools4.ts.orderbook.route

import me.devtools4.ts.cmd.CommandDispatcher
import me.devtools4.ts.dto._

case class OrderBookCmdRoutes(cmdDispatcher: CommandDispatcher[OrderBookCommand])
                             (implicit cc: castor.Context, log: cask.Logger) extends cask.Routes {
  @cask.post("/order")
  def post(request: cask.Request): String = {
    upickle.default.read[OrderBookCommand](request.text()) match {
      case cmd @ BidCommand(_, _, _, _, _) => cmdDispatcher.send(cmd)
      case cmd @ AskCommand(_, _, _, _, _) => cmdDispatcher.send(cmd)
      case _ => ???
    }
    upickle.default.write(SuccessResponse("done"))
  }

  @cask.patch("/order")
  def patch(request: cask.Request): String = {
    upickle.default.read[OrderBookCommand](request.text()) match {
      case cmd @ AmendCommand(_, _, _, _) => cmdDispatcher.send(cmd)
      case _ => ???
    }
    upickle.default.write(SuccessResponse("done"))
  }

  @cask.delete("/order")
  def delete(request: cask.Request): String = {
    upickle.default.read[OrderBookCommand](request.text()) match {
      case cmd @ DeleteCommand(_) => cmdDispatcher.send(cmd)
      case _ => ???
    }
    upickle.default.write(SuccessResponse("done"))
  }

  initialize()
}