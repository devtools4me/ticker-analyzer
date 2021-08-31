package me.devtools4.ticker

import java.io.{BufferedOutputStream, ByteArrayInputStream, FileOutputStream, InputStream, OutputStream}
import java.nio.charset.StandardCharsets
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

package object ops {
  case class ResourceName(value: String)

  implicit class ResourceNameHelper(x: ResourceName) {
    def str: String = Source.fromResource(x.value).mkString
  }

  case class FileName(value: String)

  implicit class FileNameHelper(x: FileName) {
    def toOS[A](f: OutputStream => A): Try[A] =
      Using(new BufferedOutputStream(new FileOutputStream(x.value)))(f)
  }

  implicit class StringHelper(s: String) {
    def toIS[A](f: InputStream => A): Try[A] =
      Using(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)))(f)
  }

  implicit class EitherHelper[E, A](val either: Either[E, A]) extends AnyVal {
    def _toTry: Try[A] = either.fold(t => Failure(new RuntimeException(s"$t")), Success(_))
  }

  implicit class TryHelper[A](val x: Try[A]) extends AnyVal {
    def _toEither: Either[String, A] = x.fold(t => Left(s"$t"), Right(_))
  }
}