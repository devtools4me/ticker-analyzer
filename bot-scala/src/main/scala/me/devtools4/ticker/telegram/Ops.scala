package me.devtools4.ticker.telegram

import com.github.mustachejava.{DefaultMustacheFactory, Mustache}
import me.devtools4.ticker.api.{HISTORY, QUOTE, SMA}
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.{SendChatAction, SendMessage, SendPhoto}
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

import java.io.ByteArrayInputStream
import scala.jdk.CollectionConverters._

object Ops {
  def typing(cid: Long): SendChatAction = SendChatAction.builder()
    .chatId(cid.toString)
    .action(ActionType.TYPING.toString)
    .build()

  def editMessageText(cid: Long, mid: Integer, html: String): EditMessageText = EditMessageText.builder
    .chatId(cid.toString)
    .messageId(mid)
    .text(html)
    .parseMode("HTML")
    .build

  def sendMessage(cid: Long): SendMessage = SendMessage.builder()
    .chatId(cid.toString)
    .text("What would you like to receive?")
    .replyMarkup(
      InlineKeyboardMarkup.builder.keyboardRow(List(
        InlineKeyboardButton.builder
          .text(QUOTE)
          .callbackData(QUOTE)
          .build,
        InlineKeyboardButton.builder
          .text(HISTORY)
          .callbackData(HISTORY)
          .build,
        InlineKeyboardButton.builder
          .text(SMA)
          .callbackData(SMA)
          .build
      ).asJava).build)
    .build()

  def sendMessage2(cid: Long, html: String): SendMessage = SendMessage.builder()
    .chatId(cid.toString)
    .text(html)
    .parseMode("HTML")
    .build()

  def sendPhoto(cid: Long, bytes: Array[Byte], fileName: String): SendPhoto = SendPhoto.builder()
    .chatId(cid.toString)
    .photo(new InputFile(new ByteArrayInputStream(bytes), fileName))
    .build()
}

object MustacheOps {
  lazy val mf = new DefaultMustacheFactory
  lazy val quote: Mustache = mf.compile("quote.mustache")
}