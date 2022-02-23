package me.devtools4.ts.domain

abstract class AggregateRoot[E] {
  protected var id: String
  protected var version: Int = -1
  private var changes: List[E] = List()

  def uncommittedChanges: List[E] = changes

  def markChangesAsCommitted(): Unit = {
    changes = List()
  }

  def riseEvent(e: E): Unit = applyChange(e, isNew = true)

  def replyEvents(events: List[E]): Unit = events.foreach(x => applyChange(x, isNew = false))

  protected def applyChange(e: E, isNew: Boolean): Unit = {
    try {
      apply(e)
    } finally {
      changes = changes :+ e
    }
  }

  protected def apply(e: E): Unit
}
