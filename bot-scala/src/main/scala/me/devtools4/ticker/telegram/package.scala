package me.devtools4.ticker

import me.devtools4.ticker.api.{Start, TickerCmd, TickerQuery, UnknownCmd, UnknownQuery}
import org.telegram.telegrambots.meta.api.objects.Update

package object telegram {
  sealed trait TelegramUpdate

  case class UnknownUpdate(s: String) extends TelegramUpdate

  case class TextUpdate(chatId: Long, cmd: TickerCmd) extends TelegramUpdate

  case class QueryCallback(chatId: Long, messageId: Integer, query: TickerQuery) extends TelegramUpdate

  object TelegramUpdate {
    def apply(update: Update): TelegramUpdate = update match {
      case u if (u.hasMessage && u.getMessage.hasText) => TickerCmd(u.getMessage.getText) match {
        case UnknownCmd(s) => UnknownUpdate(s)
        case x => TextUpdate(u.getMessage.getChatId, x)
      }
      case u if (u.hasCallbackQuery) => TickerQuery(u.getCallbackQuery.getMessage.getText) match {
        case UnknownQuery(s) => UnknownUpdate(s)
        case query => QueryCallback(
          u.getCallbackQuery.getMessage.getChatId,
          u.getCallbackQuery.getMessage.getMessageId,
          query)
      }
      case _ => UnknownUpdate(update.toString)
    }
  }
}