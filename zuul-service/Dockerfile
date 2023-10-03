FROM openjdk:12-alpine

MAINTAINER yo

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    PORT=9012 \
    CONFIG_PORT=8888 \
    PROFILES="default"

ADD /target/*-exec.jar /zuul-service.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /zuul-service.jar --spring.profiles.active=$PROFILES"]

EXPOSE $PORT