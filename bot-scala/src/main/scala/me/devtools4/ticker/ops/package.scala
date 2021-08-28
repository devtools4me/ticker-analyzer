package me.devtools4.ticker

import java.io.{BufferedOutputStream, ByteArrayInputStream, FileOutputStream, InputStream, OutputStream}
import java.nio.charset.StandardCharsets
import scala.io.Source
import scala.util.{Try, Using}

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
}