package me.devtools4.ts

package object query {
  trait QueryHandler[Q, E] {
    def handle(query: Q): List[E]
  }

  trait QueryRepository[E] {
    def findById(id: Long): List[E]
    def findAll(): List[E]
  }
}