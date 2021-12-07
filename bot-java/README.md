## Ticker Analyzer Bot Java

## Build

```
mvn clean install
```

## Run

```
--spring.config.location=conf/application.yml
```

## Heroku

### Install

```
cd service
heroku login
heroku container:login
heroku apps:list
heroku apps:create ticker-analyzer
docker build --build-arg bot_token=${BOT_TOKEN} --build-arg av_token=${AV_TOKEN} -t registry.heroku.com/ticker-analyzer/web .
docker images
docker push registry.heroku.com/ticker-analyzer/web
heroku container:release web --app=ticker-analyzer
heroku open --app=ticker-analyzer
https://ticker-analyzer.herokuapp.com/check
heroku logs --app ticker-analyzer
```

### Destroy

```
heroku apps:list
heroku apps:destroy ticker-analyzer
```
