FROM openjdk:11.0-jdk-oracle

RUN mkdir -p /apps
COPY ./target/*.jar /apps/app.jar
COPY ./conf/application.yml /apps/application.yml
COPY ./conf/all.csv /apps/all.csv
COPY ./entrypoint.sh /apps/entrypoint.sh

RUN chmod +x /apps/entrypoint.sh

CMD ["/apps/entrypoint.sh"]