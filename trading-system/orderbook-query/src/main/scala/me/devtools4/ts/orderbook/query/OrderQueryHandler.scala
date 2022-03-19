package me.devtools4.ts.orderbook.query

import me.devtools4.ts.orderbook.model.OrderEntity
import me.devtools4.ts.query.{FindAllOrderBooksQuery, FindOrdersByIdQuery, OrderQuery, QueryHandler, QueryRepository}

class OrderQueryHandler(repository: QueryRepository[OrderEntity]) extends QueryHandler[OrderQuery, OrderEntity] {
  override def handle(query: OrderQuery): List[OrderEntity] = query match {
    case FindOrdersByIdQuery(id) => repository.findById(id)
    case FindAllOrderBooksQuery() => repository.findAll()
    case _ => ???
  }
}

object OrderQueryHandler {
  def apply(repository: QueryRepository[OrderEntity]) = new OrderQueryHandler(repository)
}