package me.devtools4.ticker.telegram

import me.devtools4.ticker.api._
import me.devtools4.ticker.telegram.Ops.{editMessageText, sendMessage, typing}
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

import scala.io.Source
import scala.util.{Failure, Using}

class WebHookBot(path: String, user: String, token: String) extends TelegramWebhookBot {
  val QUOTE = "/quote"
  val HISTORY = "/history"
  val SMA = "/sma"

  override def onWebhookUpdateReceived(update: Update): BotApiMethod[_] = {
    TelegramUpdate(update) match {
      case TextUpdate(cid, cmd) => handle(cid, cmd)
      case QueryCallback(cid, mid, q) => query(cid, mid, q)
      case _ =>
    }
    null
  }

  override def getBotPath: String = path

  override def getBotUsername: String = user

  override def getBotToken: String = token

  private def handle(chatId: Long, cmd: TickerCmd): Unit = {
    execute(typing(chatId))

    cmd match {
      case Start => execute(sendMessage(chatId))
      case Quote(symbol) =>
      case History(symbol, period) =>
      case Sma(symbol, period) =>
    }
  }

  private def query(chatId: Long, messageId: Integer, query: TickerQuery): Unit = {
    execute(typing(chatId))

    html(query).map { html =>
      execute(editMessageText(chatId, messageId, html))
    }
  }

  private def html(query: TickerQuery) = query match {
    case QuoteQuery => html("quote.html")
    case HistoryQuery => html("history.html")
    case SmaQuery => html("sma.html")
    case _ => Failure(new IllegalArgumentException(TickerQuery.toString))
  }

  private def html(res: String) =
    Using(getClass.getClassLoader.getResourceAsStream(res)) { is =>
      Source.fromInputStream(is).mkString
    }
}