## Setup env

Original:
```
https://blog.miguelgrinberg.com/post/setting-up-a-flask-application-in-pycharm
```

```
cd webapp
python -m venv venv
. venv/bin/activate
pip install -r req/requirements.txt
export FLASK_APP=app.py
```

## Heroku

### Install

```
heroku container:login
heroku create
heroku apps:list
heroku container:push web -a <APP_NAME>
heroku container:release web
heroku open
```

### Destroy

```
heroku apps:list
heroku apps:destroy <APP_NAME>
```

## IDE Run

Settings:

```
Python interpeter: ./bot/webapp/env/bin/python3
```

Run configuration:

```
Name: webapp
Script path: ./venv/bin/flask
Parameters: run
Environment variables: PYTHONUNBUFFERED=1;FLASK_APP=app.py
Python interpeter: ./bot/webapp/env/bin/python3
Working dir: ./bot/webapp
```

## Docker

```
docker build -t asa/telebot .
docker run -p 5000:5000 -e PORT=5000 asa/telebot
```