## PostgreSQL

```
docker run -d --name postgres13 \
    -v postgres_data:/var/lib/postgresql/data \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=postgres \
    -p 5432:5432 \
    postgres:latest
```

## pgAdmin

```
docker run -d --name pgAdmin \
    -v pgadmin_data:/var/lib/pgadmin \
    -e 'PGADMIN_DEFAULT_EMAIL=user@user.com' \
    -e 'PGADMIN_DEFAULT_PASSWORD=admin' \
    --add-host=host.docker.internal:host-gateway \
    -p 54321:80 \
    dpage/pgadmin4
```

### Open pgAdmin

```
127.0.0.1:54321
user: user@user.com
password: admin
```

### Add database

```
host: host.docker.internal
port: 5432
database: postgres
user: postgres
password: postgres
```

## Kafka

```
zookeeper-server-start /opt/homebrew/etc/kafka/zookeeper.properties
kafka-server-start /opt/homebrew/etc/kafka/server.properties
```