package me.devtools4.ts.orderbook.domain

import me.devtools4.ts.dto.Order

import scala.collection.mutable

trait Container[T] {
  def first: Option[T]
  def add(t: T): Unit
  def update(t: T): Unit
  def delete(t: T): Unit
  def all(): List[T]
  def size: Int
}

class OrderComparator extends Ordering[Order] {
  override def compare(o1: Order, o2: Order): Int = ???
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
  def findTradable() = ???
  override def toString: String = s"bids(${bids.size}), asks(${asks.size})"
}
