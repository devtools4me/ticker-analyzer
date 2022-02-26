package me.devtools4.ts

import me.devtools4.ts.dto.Version

package object domain {
  abstract class AggregateRoot[E] {
    protected var id: String
    protected var version: Version = new Version()
    private var changes: List[E] = List()

    def getId: String = id

    def getVersion: Version = version

    def setVersion(v: Version): Unit = {
      version = v
    }

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

  trait EventSourcingHandler[E, T] {
    def save(aggregate: AggregateRoot[E]): Unit
    def find(id: String): T
  }
}
