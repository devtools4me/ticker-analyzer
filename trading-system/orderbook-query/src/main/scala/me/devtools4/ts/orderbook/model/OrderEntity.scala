package me.devtools4.ts.orderbook.model

case class OrderEntity(id: Long)

import scalikejdbc._

object OrderEntity extends SQLSyntaxSupport[OrderEntity] {
  override val tableName = "order_books"

  def apply(rs: WrappedResultSet): OrderEntity = OrderEntity(
    rs.long("id")
  )
}