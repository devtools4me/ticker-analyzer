package com.yahoo.finanance.query1

import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

import scala.io.Source
import io.circe.parser._
import io.circe.syntax._

@RunWith(classOf[JUnitRunner])
class QuoteTest  extends AnyFunSuite {
  test("quote decode") {
    val json = Source.fromResource("INTC.json").mkString
    val q = decode[QuoteResponseResponse](json)
    assertResult(true)(q.isRight)
//    q.foreach { x =>
//      assertResult(json)(x.asJson)
//    }
  }
}