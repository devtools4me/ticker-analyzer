package me.devtools4.ticker

import com.bot4s.telegram.models.Update
import me.devtools4.ticker.api.{TickerCmd, TickerQuery, UnknownCmd, UnknownQuery}

package object telegram {

  sealed trait TelegramUpdate

  case class UnknownUpdate(s: String) extends TelegramUpdate

  case class TextUpdate(chatId: Long, cmd: TickerCmd) extends TelegramUpdate

  case class QueryCallback(chatId: Long, messageId: Integer, query: TickerQuery) extends TelegramUpdate

  object TelegramUpdate {
    def apply(update: Update): TelegramUpdate = update match {
      case Update(_, Some(m), _, _, _, _, _, None, _, _, _, _, _) if m.text.isDefined =>
        TickerCmd(m.text.get) match {
          case UnknownCmd(s) => UnknownUpdate(s)
          case x => TextUpdate(m.chat.id, x)
        }
      case Update(_, None, _, _, _, _, _, Some(cb), _, _, _, _, _) if cb.message.exists(x => x.text.isDefined) =>
        TickerQuery(cb.message.get.text.get) match {
          case UnknownQuery(s) => UnknownUpdate(s)
          case query => QueryCallback(
            cb.message.get.chat.id,
            cb.message.get.messageId,
            query)
        }
      case _ => UnknownUpdate(update.toString)
    }
  }
}