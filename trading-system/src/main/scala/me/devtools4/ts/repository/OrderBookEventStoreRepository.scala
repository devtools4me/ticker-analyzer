package me.devtools4.ts.repository

import me.devtools4.ts.api.EventStoreRepository
import me.devtools4.ts.model.OrderBookEventEntity

class OrderBookEventStoreRepository extends EventStoreRepository[OrderBookEventEntity] {
  import scalikejdbc._

  implicit val session = AutoSession

  override def save(e: OrderBookEventEntity): Option[OrderBookEventEntity] = {
    val te = OrderBookEventEntity.syntax
    sql"""
    insert into ${OrderBookEventEntity.as(te)}
    (created_at, aggregate_id, aggregate_type, version,event_type, event_data)
    values
    (${e.createdAt}, ${e.aggregateId}, ${e.aggregateType}, ${e.version}, ${e.eventType}, ${e.eventData})
    """.update.apply()
    Some(e)
  }

  override def find(id: Long): Option[OrderBookEventEntity] = {
    val e = OrderBookEventEntity.syntax
    sql"select * from ${OrderBookEventEntity.as(e)} where ${e.id} = ${id}"
      .map(OrderBookEventEntity(_))
      .first.apply()
  }

  override def findByAggregateId(aggregateId: String): List[OrderBookEventEntity] = {
    val e = OrderBookEventEntity.syntax
    sql"select * from ${OrderBookEventEntity.as(e)}"
      .map(OrderBookEventEntity(_))
      .list.apply()
  }
}
