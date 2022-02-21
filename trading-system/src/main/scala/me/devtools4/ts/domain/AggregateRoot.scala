package me.devtools4.ts.domain

import me.devtools4.ts.api.Event

abstract class AggregateRoot {
  protected var id: String
  protected var version: Int = -1
  private var changes: List[Event] = List()

  def uncommittedChanges: List[Event] = changes

  def markChangesAsCommitted(): Unit = {
    changes = List()
  }

  protected def apply(e: Event): Unit

  protected def applyChange(e: Event, isNew: Boolean): Unit = {
    try {
      apply(e)
    } finally {
      changes = changes :+ e
    }
  }

  def riseEvent(e: Event): Unit = applyChange(e, isNew = true)

  def replyEvents(events: List[Event]): Unit = events.foreach(x => applyChange(x, isNew = false))
}
