package me.devtools4.ts.orderbook.model

import me.devtools4.ts.dto.OrderBookEvent

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
  override val tableName = "EVENTS"

  def apply(rs: WrappedResultSet): OrderBookEventEntity = new OrderBookEventEntity(
    rs.long("id"),
    rs.zonedDateTime("created_at"),
    rs.string("aggregate_id"),
    rs.string("aggregate_type"),
    rs.int("version"),
    rs.string("event_type"),
    OrderBookEvent(rs.string("event_data")))

  def apply(createdAt: ZonedDateTime,
            aggregateId: String,
            aggregateType: String,
            version: Int,
            eventType: String,
            eventData: OrderBookEvent): OrderBookEventEntity = new OrderBookEventEntity(
    0,
    createdAt,
    aggregateId,
    aggregateType,
    version,
    eventType,
    eventData
  )
}

