package me.devtools4.ts.orderbook.domain

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec

class OrderBookSpec extends AnyFlatSpec
  with BeforeAndAfterAll
  with LazyLogging {
  private var sut: OrderBook = _
  override def beforeAll(): Unit = {
    sut = OrderBook(OrderContainer(), OrderContainer())
  }

  "Bid order" should "be submitted" in {

  }
}