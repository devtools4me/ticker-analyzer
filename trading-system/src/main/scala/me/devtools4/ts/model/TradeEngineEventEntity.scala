package me.devtools4.ts.model

import me.devtools4.ts.api.{EventStoreRepository, TradeEngineEvent, TradeEngineStoppedEvent}

import java.time.ZonedDateTime

case class TradeEngineEventEntity(id: Long,
                                  createdAt: ZonedDateTime,
                                  aggregateId: String,
                                  aggregateType: String,
                                  version: Int,
                                  eventType: String,
                                  eventData: TradeEngineEvent)

import scalikejdbc._

object TradeEngineEventEntity extends SQLSyntaxSupport[TradeEngineEventEntity] {
  override val tableName = "events"

  def apply(rs: WrappedResultSet): TradeEngineEventEntity = TradeEngineEventEntity(
    rs.long("id"),
    rs.zonedDateTime("created_at"),
    rs.string("aggregate_id"),
    rs.string("aggregate_type"),
    rs.int("version"),
    rs.string("event_type"),
    null)
}
