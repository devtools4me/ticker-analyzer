import re
from flask import Flask, request
import telegram
from credentials import bot_token, bot_user_name, URL
import yfinance as yf

global bot
global TOKEN
TOKEN = bot_token
bot = telegram.Bot(token=TOKEN)

app = Flask(__name__)


@app.route('/{}'.format(TOKEN), methods=['GET', 'POST'])
def respond():
    update = telegram.Update.de_json(request.get_json(force=True), bot)
    chat_id = update.message.chat.id
    msg_id = update.message.message_id
    text = update.message.text.encode('utf-8').decode()
    print("got text message :", text)

    if text == "/start":
        bot_welcome = """
        Welcome to coolAvatar bot, the bot is using the service from http://avatars.adorable.io/ to generate cool looking avatars based on the name you enter so please enter a name and the bot will reply with an avatar for your name.
        """
        bot.sendMessage(chat_id=chat_id, text=bot_welcome, reply_to_message_id=msg_id)
    else:
        try:
            # clear the message we got from any non alphabets
            text = re.sub(r"\W", "_", text)
            tic = yf.Ticker(text.strip())
            bot.sendMessage(chat_id=chat_id, text=str(tic.info), reply_to_message_id=msg_id)
        except Exception:
            # if things went wrong
            bot.sendMessage(chat_id=chat_id,
                            text="There was a problem in the name you used, please enter different name",
                            reply_to_message_id=msg_id)
    return 'ok'


@app.route('/set_webhook', methods=['GET', 'POST'])
def set_webhook():
    hook_url = '{URL}{HOOK}'.format(URL=URL, HOOK=TOKEN)
    print("hook_url=", hook_url)

    s = bot.setWebhook(hook_url)
    if s:
        return "webhook setup ok"
    else:
        return "webhook setup failed, hook_url=" + hook_url


@app.route('/')
def index():
    return '.'


if __name__ == '__main__':
    app.run(host='0.0.0.0', threaded=True)
