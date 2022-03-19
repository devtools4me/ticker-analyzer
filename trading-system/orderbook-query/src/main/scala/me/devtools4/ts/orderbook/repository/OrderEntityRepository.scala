package me.devtools4.ts.orderbook.repository

import me.devtools4.ts.orderbook.model.OrderEntity
import me.devtools4.ts.query.QueryRepository

class OrderEntityRepository extends QueryRepository[OrderEntity] {
  override def findById(id: Long): List[OrderEntity] = ???

  override def findAll(): List[OrderEntity] = ???
}

object OrderEntityRepository {
  def apply() = new OrderEntityRepository()
}