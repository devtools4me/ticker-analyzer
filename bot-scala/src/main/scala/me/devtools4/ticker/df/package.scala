package me.devtools4.ticker

import java.io.{BufferedOutputStream, ByteArrayInputStream, FileOutputStream, InputStream, OutputStream}
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import scala.util.{Try, Using}

package object df {

  case class Etf(symbol: String,
                 startDate: LocalDate,
                 name: String,
                 segment: String,
                 issuer: String,
                 expenseRatio: String,
                 aum: String)

  def str2is[A](s: String)(f: InputStream => A): Try[A] =
    Using(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)))(f)

  def fos4any[A](fileName: String)(f: OutputStream => A) =
    Using(new BufferedOutputStream(new FileOutputStream(fileName)))(f)
}