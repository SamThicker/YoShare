FROM openjdk:12-alpine

MAINTAINER yo

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    PORT=9010 \
    CONFIG_PORT=8888 \
    PROFILES="default"

ADD /target/*-exec.jar /member-service.jar

RUN apk update && apk add curl && rm -rf /var/chche/apk/*

HEALTHCHECK --interval=5s --timeout=30s CMD curl -f http://localhost:$PORT/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /member-service.jar --spring.profiles.active=$PROFILES"]

EXPOSE $PORT