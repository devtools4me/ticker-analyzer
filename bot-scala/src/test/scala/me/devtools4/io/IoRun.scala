package me.devtools4.io

import cats.effect.unsafe.IORuntime
import cats.effect.{IO, IOApp}

object IoRun extends App {
  implicit val runtime: IORuntime = cats.effect.unsafe.implicits.global

  def putStrLn(line: String): IO[Unit] =
    IO { println(line) }

  def f(f1: IO[Unit], f2: IO[Unit]): IO[Unit] = {
    for {
      _ <- f1
      _ <- f2
    } yield ()
  }

  f(putStrLn("hi!"), putStrLn("hi!"))
    .unsafeRunSync()

  val x = putStrLn("hi2")
  f(x, x)
    .unsafeRunSync()
}
