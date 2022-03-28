package me.devtools4.ts.orderbook

import me.devtools4.ts.config.ServiceConfig
import me.devtools4.ts.orderbook.route.OrderBookCmdRoutes

object OrderBookCmdApp extends cask.Main {
  import pureconfig._
  import pureconfig.generic.auto._

  private val ctx = ConfigSource.resources("application.conf").load[ServiceConfig] match {
    case Right(conf) => AppContext(conf)
    case Left(error) => throw new Exception(error.toString())
  }

  val allRoutes = Seq(OrderBookCmdRoutes(ctx.cmdDispatcher))
}