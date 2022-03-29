package me.devtools4.ts.orderbook.route

import com.typesafe.scalalogging.LazyLogging
import me.devtools4.ts.cmd.CommandDispatcher
import me.devtools4.ts.dto._

case class OrderBookCmdRoutes(cmdDispatcher: CommandDispatcher[OrderBookCommand])
                             (implicit cc: castor.Context, log: cask.Logger)
  extends cask.Routes with LazyLogging {
  @cask.post("/order")
  def post(r: cask.Request): String = {
    logger.info(s"post, r=${r.text()}")
    upickle.default.read[OrderBookCommand](r.text()) match {
      case cmd @ BidCommand(_, _, _, _, _) => cmdDispatcher.send(cmd)
      case cmd @ AskCommand(_, _, _, _, _) => cmdDispatcher.send(cmd)
      case _ => ???
    }
    upickle.default.write(SuccessResponse("done"))
  }

  @cask.patch("/order")
  def patch(r: cask.Request): String = {
    logger.info(s"patch, r=${r.text()}")
    upickle.default.read[OrderBookCommand](r.text()) match {
      case cmd @ AmendCommand(_, _, _, _) => cmdDispatcher.send(cmd)
      case _ => ???
    }
    upickle.default.write(SuccessResponse("done"))
  }

  @cask.delete("/order")
  def delete(request: cask.Request): String = {
    logger.info(s"delete, r=${request.text()}")
    upickle.default.read[OrderBookCommand](request.text()) match {
      case cmd @ DeleteCommand(_) => cmdDispatcher.send(cmd)
      case _ => ???
    }
    upickle.default.write(SuccessResponse("done"))
  }

  initialize()
}