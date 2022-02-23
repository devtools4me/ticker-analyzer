package me.devtools4.ts.model

import me.devtools4.ts.api.{EventStoreRepository, OrderBookEvent, OrderBookStoppedEvent}

import java.time.ZonedDateTime

case class OrderBookEventEntity(id: Long,
                                createdAt: ZonedDateTime,
                                aggregateId: String,
                                aggregateType: String,
                                version: Int,
                                eventType: String,
                                eventData: OrderBookEvent)

import scalikejdbc._

object OrderBookEventEntity extends SQLSyntaxSupport[OrderBookEventEntity] {
  override val tableName = "events"

  def apply(rs: WrappedResultSet): OrderBookEventEntity = OrderBookEventEntity(
    rs.long("id"),
    rs.zonedDateTime("created_at"),
    rs.string("aggregate_id"),
    rs.string("aggregate_type"),
    rs.int("version"),
    rs.string("event_type"),
    null)
}
