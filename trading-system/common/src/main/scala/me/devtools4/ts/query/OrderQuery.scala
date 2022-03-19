package me.devtools4.ts.query

sealed trait OrderQuery

case class FindOrdersByIdQuery(id: Long) extends OrderQuery

case class FindAllOrderBooksQuery() extends OrderQuery