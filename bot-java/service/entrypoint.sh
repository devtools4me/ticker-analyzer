#!/usr/bin/env sh

java -XX:+UseG1GC -Xms256m -Xmx512m -jar /apps/app.jar --spring.config.location=/apps/application.yml