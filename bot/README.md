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

## Heroku Run

```
heroku container:login
heroku create
heroku container:push web
heroku container:release web
heroku open
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