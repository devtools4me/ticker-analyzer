FROM openjdk:16-jdk-alpine

RUN mkdir -p /apps
COPY ./target/*.jar /apps/app.jar
COPY ./entrypoint.sh /apps/entrypoint.sh

RUN chmod +x /apps/entrypoint.sh
CMD ["/apps/entrypoint.sh"]