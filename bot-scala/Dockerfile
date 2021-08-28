FROM openjdk:11.0-jdk-oracle

RUN mkdir -p /apps
COPY ./build/libs/*.jar /apps/app.jar
COPY ./conf/application.conf /apps/application.conf
COPY ./entrypoint.sh /apps/entrypoint.sh

RUN chmod +x /apps/entrypoint.sh

CMD ["/apps/entrypoint.sh"]