package me.devtools4.ts.orderbook.domain

import me.devtools4.ts.dto.Order

import scala.collection.mutable
import scala.util.Properties

trait Container[T] {
  def first: Option[T]
  def add(t: T): Unit
  def update(t: T): Unit
  def delete(t: T): Unit
  def all(): List[T]
  def size: Int
}

class OrderComparator extends Ordering[Order] {
  override def compare(o1: Order, o2: Order): Int = (o1, o2) match {
    case (x, y) if x == y => 0
    case (x, y) if x.ticker != y.ticker => throw new IllegalArgumentException
    case (x, y) if x.side != y.side => throw new IllegalArgumentException
    case (x, y) if x.equals(y) => 0
    case (x, y) if x.price.compareTo(y.price) > 0 && x.isBid => -1
    case (x, y) if x.price.compareTo(y.price) > 0 && x.isAsk => 1
    case (x, y) if x.price.compareTo(y.price) < 0 && x.isBid => 1
    case (x, y) if x.price.compareTo(y.price) < 0 && x.isAsk => -1
    case (x, y) if x.time == y.time => 0
    case (x, y) if x.time < y.time => -1
    case (_, _) => 1
  }
}

object OrderComparator {
  def apply() = new OrderComparator
}

class OrderContainer extends Container[Order] {
  private val orders: mutable.TreeSet[Order] = mutable.TreeSet.empty(new OrderComparator())

  override def first: Option[Order] = orders.headOption

  override def add(t: Order): Unit = orders.add(t)

  override def update(t: Order): Unit = {
    orders.remove(t)
    orders.add(t)
  }

  override def delete(t: Order): Unit = orders.remove(t)

  override def all(): List[Order] = orders.toList

  override def size: Int = orders.size
}

object OrderContainer {
  def apply() = new OrderContainer
}

case class OrderBook(bids: Container[Order], asks: Container[Order]) {
  def add(order: Order): Unit = if (order.isBid) bids.add(order) else asks.add(order)
  def add(orders: List[Order]): Unit = orders.foreach(add)
  def findTradable() = ???
  def str: String = {
    val allBids = bids.all().map(_.str)
    val allAsks = asks.all().map(_.str)
    allBids.zipAll(allAsks, Order.emptyStr, Order.emptyStr).foldLeft("") {
      case (acc, (o1, o2)) =>
        if (acc.isEmpty)
          s"$o1 | $o2"
        else
          acc + Properties.lineSeparator + s"$o1 | $o2"
    }
  }
  override def toString: String = s"bids(${bids.size}), asks(${asks.size})"
}
