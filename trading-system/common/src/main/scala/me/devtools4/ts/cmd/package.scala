package me.devtools4.ts

import com.typesafe.scalalogging.LazyLogging

package object cmd {

  trait CommandDispatcher[C] {
    def send(cmd: C): Unit
  }

  object CommandDispatcher extends LazyLogging {
    def apply[C](handler: CommandHandler[C]): CommandDispatcher[C] = (cmd: C) => {
      logger.info(s"dispatch, cmd=$cmd")
      handler.handle(cmd)
    }
  }

  trait CommandHandler[C] {
    def handle(cmd: C): Unit
  }
}
