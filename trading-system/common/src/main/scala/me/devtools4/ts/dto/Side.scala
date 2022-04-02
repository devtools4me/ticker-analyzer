package me.devtools4.ts.dto

import upickle.default.{ReadWriter, macroRW}

sealed trait Side {
  val value: String
}

object Side {
  implicit val rw: ReadWriter[Side] = macroRW

  def toSide(s: String): Option[Side] = s match {
    case Bid.value => Some(Bid)
    case Ask.value => Some(Ask)
    case _ => None
  }
}

case object Bid extends Side {
  override val value: String = "B"
}

case object Ask extends Side {
  override val value: String = "S"
}