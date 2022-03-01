package me.devtools4.ts

package object cmd {

  trait CommandDispatcher[C] {
    def send(cmd: C): Unit
  }

  object CommandDispatcher {
    def apply[C](handler: CommandHandler[C]): CommandDispatcher[C] = (cmd: C) =>
      handler.handle(cmd)
  }

  trait CommandHandler[C] {
    def handle(cmd: C): Unit
  }
}
