package me.devtools4.ticker.telegram

import com.yahoo.finanance.query1.Quote
import com.yahoo.finanance.query1.Quote._
import me.devtools4.ticker.api._
import me.devtools4.ticker.df.str2is
import me.devtools4.ticker.service.TickerService
import me.devtools4.ticker.telegram.Ops._
import org.telegram.telegrambots.meta.api.objects.Update

import scala.io.Source
import scala.util.{Failure, Try}

class UpdateHandler(ts: TickerService) {
  def handle(update: Update, consumer: ApiMethodConsumer): Unit = {
    TelegramUpdate(update) match {
      case TextUpdate(cid, cmd) =>
        consumer.accept(typing(cid))
        cmd match {
          case StartCmd => consumer.accept(sendMessage(cid))
          case QuoteCmd(sym) => ts.quote(sym)
            .flatMap(x => x.as[String].toOption)
            .map(sendMessage2(cid, _))
            .foreach(consumer.accept)
          case HistoryCmd(sym, period) => ts.history(sym, period)
            .map(sendPhoto(cid, _, s"$sym.png"))
            .foreach(consumer.accept)
          case SmaCmd(sym, period) => ts.sma(sym, period)
            .map(sendPhoto(cid, _, s"$sym.png"))
            .foreach(consumer.accept)
          case _ => Quote.error(new IllegalArgumentException(cmd.toString))
            .map(sendMessage2(cid, _))
            .foreach(consumer.accept)
        }
      case QueryCallback(cid, mid, q) =>
        consumer.accept(typing(cid))
        html(q)
          .map(editMessageText(cid, mid, _))
          .foreach(consumer.accept)
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
    str2is(res) { is =>
      Source.fromInputStream(is).mkString
    }
}