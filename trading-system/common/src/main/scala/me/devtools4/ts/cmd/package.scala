package me.devtools4.ts

package object cmd {

  trait CommandHandler[C] {
    def handle(cmd: C): Unit
  }
}
