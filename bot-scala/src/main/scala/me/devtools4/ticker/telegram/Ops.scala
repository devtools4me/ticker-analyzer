package me.devtools4.ticker.telegram

import com.bot4s.telegram.methods._
import com.bot4s.telegram.models._
import me.devtools4.ticker.api.{HISTORY, QUOTE, SMA}

object Ops {

  def typing(cid: Long): SendChatAction = SendChatAction(ChatId(cid), ChatAction.Typing)

  def editMessageText(cid: Long, mid: Integer, html: String): EditMessageText = EditMessageText(
    chatId = Some(ChatId(cid)),
    messageId = Some(mid),
    text = html,
    parseMode = Some(ParseMode.HTML)
  )

  def sendMessage(cid: Long): SendMessage = SendMessage(
    chatId = ChatId(cid),
    text = "What would you like to receive?",
    replyMarkup = Some(InlineKeyboardMarkup(List(List(
      InlineKeyboardButton(QUOTE, Some(QUOTE)),
      InlineKeyboardButton(HISTORY, Some(HISTORY)),
      InlineKeyboardButton(SMA, Some(SMA))
    )))))

  def sendMessage2(cid: Long, html: String): SendMessage = SendMessage(
    chatId = ChatId(cid),
    text = html,
    Some(ParseMode.HTML)
  )

  def sendPhoto(cid: Long, bytes: Array[Byte], fileName: String): SendPhoto = SendPhoto(
    ChatId(cid),
    InputFile(fileName, bytes)
  )
}

