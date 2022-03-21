package me.devtools4.ts.orderbook

import me.devtools4.ts.config.ServiceConfig
import me.devtools4.ts.dto._

object OrderBookCmdApp extends cask.MainRoutes {
  import pureconfig._
  import pureconfig.generic.auto._

  private val ctx = ConfigSource.resources("application.conf").load[ServiceConfig] match {
    case Right(conf) => AppContext(conf)
    case Left(error) => throw new Exception(error.toString())
  }
  private val cmdDispatcher = ctx.cmdDispatcher

  @cask.post("/order")
  def post(request: cask.Request): String = {
    upickle.default.read[OrderBookCommand](request.text()) match {
      case cmd @ BidCommand(_, _, _, _) => cmdDispatcher.send(cmd)
      case cmd @ AskCommand(_, _, _, _) => cmdDispatcher.send(cmd)
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

  @cask.delete("/delete")
  def delete(request: cask.Request): String = {
    upickle.default.read[OrderBookCommand](request.text()) match {
      case cmd @ DeleteCommand(_) => cmdDispatcher.send(cmd)
      case _ => ???
    }
    upickle.default.write(SuccessResponse("done"))
  }

  initialize()
}