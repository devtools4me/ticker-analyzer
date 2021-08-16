package me.devtools4.ticker.telegram

import me.devtools4.ticker.api._
import me.devtools4.ticker.telegram.Ops._
import org.telegram.telegrambots.meta.api.objects.Update

import scala.io.Source
import scala.util.{Failure, Try, Using}

class UpdateHandler() {

  def handle(update: Update, consumer: ApiMethodConsumer): Unit = {
    TelegramUpdate(update) match {
      case TextUpdate(cid, cmd) =>
        consumer.accept(typing(cid))
        cmd match {
          case Start => consumer.accept(sendMessage(cid))
          case Quote(sym) => consumer.accept(sendMessage2(cid, null))
          case History(sym, period) => consumer.accept(sendPhoto(cid, null, s"$sym.png"))
          case Sma(sym, period) => consumer.accept(sendPhoto(cid, null, s"$sym.png"))
          case _ =>
        }
      case QueryCallback(cid, mid, q) =>
        consumer.accept(typing(cid))
        html(q).map { html =>
          consumer.accept(editMessageText(cid, mid, html))
        }
      case _ =>
    }
  }

  private def html(query: TickerQuery): Try[String] = query match {
    case QuoteQuery => html("quote.html")
    case HistoryQuery => html("history.html")
    case SmaQuery => html("sma.html")
    case _ => Failure(new IllegalArgumentException(TickerQuery.toString))
  }

  private def html(res: String): Try[String] =
    Using(getClass.getClassLoader.getResourceAsStream(res)) { is =>
      Source.fromInputStream(is).mkString
    }
}