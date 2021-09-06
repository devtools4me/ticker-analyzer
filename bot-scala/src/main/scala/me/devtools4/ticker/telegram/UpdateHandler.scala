package me.devtools4.ticker.telegram

import com.yahoo.finanance.query1.Quote._
import me.devtools4.ticker.api._
import me.devtools4.ticker.service.TickerService
import me.devtools4.ticker.telegram.Ops._
import com.bot4s.telegram.methods._
import com.bot4s.telegram.models._

trait ApiMethodConsumer {
  def accept(t: SendChatAction): Unit

  def accept(t: SendMessage): Unit

  def accept(t: SendPhoto): Unit

  def accept(t: EditMessageText): Unit
}

class UpdateHandler(ts: TickerService) {
  def handle(update: Update, consumer: ApiMethodConsumer): Unit = {
    TelegramUpdate(update) match {
      case TextUpdate(cid, cmd) =>
        consumer.accept(typing(cid))
        cmd match {
          case StartCmd => consumer.accept(sendMessage(cid))
          case QuoteCmd(sym) => ts.quote(sym)
            .flatMap(x => x.html.toOption)
            .map(sendMessage2(cid, _))
            .foreach(consumer.accept)
          case HistoryCmd(sym, period) => ts.history(sym, period)
            .map(sendPhoto(cid, _, s"$sym.png"))
            .foreach(consumer.accept)
          case SmaCmd(sym, period) => ts.sma(sym, period)
            .map(sendPhoto(cid, _, s"$sym.png"))
            .foreach(consumer.accept)
          case _ => new IllegalArgumentException(cmd.toString)
            .html
            .map(sendMessage2(cid, _))
            .foreach(consumer.accept)
        }
      case QueryCallback(cid, mid, q) =>
        consumer.accept(typing(cid))
        consumer.accept(editMessageText(cid, mid, html(q)))
      case _ =>
    }
  }

  import me.devtools4.ticker.ops._

  private def html(query: TickerQuery): String = query match {
    case QuoteQuery => ResourceName("quote.html").str
    case HistoryQuery => ResourceName("history.html").str
    case SmaQuery => ResourceName("sma.html").str
    case _ => ResourceName("error.html").str
  }
}

object UpdateHandler {
  def apply(ts: TickerService): UpdateHandler = new UpdateHandler(ts)
}