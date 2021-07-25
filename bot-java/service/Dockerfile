FROM openjdk:11.0-jdk-oracle

RUN mkdir -p /apps
COPY ./target/*.jar /apps/app.jar
COPY ./entrypoint.sh /apps/entrypoint.sh

RUN chmod +x /apps/entrypoint.sh

CMD ["/apps/entrypoint.sh"]