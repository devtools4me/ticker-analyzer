package com.yahoo.finanance.query1

import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

import scala.io.Source
import io.circe.parser._
import io.circe.syntax._

@RunWith(classOf[JUnitRunner])
class QuoteTest extends AnyFunSuite {
  test("quote decode") {
    List("CXSE.json", "GLD.json", "INTC.json", "VIOO.json").foreach { fn =>
      val json = Source.fromResource(fn).mkString
      val q = decode[QuoteResponseResponse](json)
      q match {
        case Left(err) => println(err)
        case _ =>
      }
      assertResult(true)(q.isRight)
    }
  }
}