package me.devtools4.ticker

import me.devtools4.ticker.api.{TickerCmd, TickerQuery, UnknownCmd, UnknownQuery}
import com.bot4s.telegram.models.Update

package object telegram {

  sealed trait TelegramUpdate

  case class UnknownUpdate(s: String) extends TelegramUpdate

  case class TextUpdate(chatId: Long, cmd: TickerCmd) extends TelegramUpdate

  case class QueryCallback(chatId: Long, messageId: Integer, query: TickerQuery) extends TelegramUpdate

  object TelegramUpdate {
    def apply(update: Update): TelegramUpdate = update match {
      case u if (u.message.exists(x => x.text.isDefined)) => TickerCmd(u.message.get.text.get) match {
        case UnknownCmd(s) => UnknownUpdate(s)
        case x => TextUpdate(u.message.get.chat.id, x)
      }
      case u if (u.callbackQuery.isDefined) => TickerQuery(u.callbackQuery.get.message.get.text.get) match {
        case UnknownQuery(s) => UnknownUpdate(s)
        case query => QueryCallback(
          u.callbackQuery.get.message.get.chat.id,
          u.callbackQuery.get.message.get.messageId,
          query)
      }
      case _ => UnknownUpdate(update.toString)
    }
  }
}