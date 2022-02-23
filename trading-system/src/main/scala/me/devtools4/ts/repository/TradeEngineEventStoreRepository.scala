package me.devtools4.ts.repository

import me.devtools4.ts.api.EventStoreRepository
import me.devtools4.ts.model.TradeEngineEventEntity

class TradeEngineEventStoreRepository extends EventStoreRepository[TradeEngineEventEntity] {
  import scalikejdbc._

  implicit val session = AutoSession

  override def save(e: TradeEngineEventEntity): Option[TradeEngineEventEntity] = {
    val te = TradeEngineEventEntity.syntax
    sql"""
    insert into ${TradeEngineEventEntity.as(te)}
    (created_at, aggregate_id, aggregate_type, version,event_type, event_data)
    values
    (${e.createdAt}, ${e.aggregateId}, ${e.aggregateType}, ${e.version}, ${e.eventType}, ${e.eventData})
    """.update.apply()
    Some(e)
  }

  override def find(id: Long): Option[TradeEngineEventEntity] = {
    val e = TradeEngineEventEntity.syntax
    sql"select * from ${TradeEngineEventEntity.as(e)} where ${e.id} = ${id}"
      .map(TradeEngineEventEntity(_))
      .first.apply()
  }

  override def findByAggregateId(aggregateId: String): List[TradeEngineEventEntity] = {
    val e = TradeEngineEventEntity.syntax
    sql"select * from ${TradeEngineEventEntity.as(e)}"
      .map(TradeEngineEventEntity(_))
      .list.apply()
  }
}
