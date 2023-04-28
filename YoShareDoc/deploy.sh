#!/bin/bash

# eureka-service
docker run --name eureka-service \
--privileged \
--network host \
-d yoshare/eureka-server:0.0.1-SNAPSHOT


# zuul-service
docker run --name zuul-service \
--privileged \
--network host \
-d yoshare/zuul-service:0.0.1-SNAPSHOT


# security-service
docker run --name security-service \
--privileged \
--network host \
-d yoshare/security-service:0.0.1-SNAPSHOT


# member-service
docker run --name member-service \
--privileged \
--network host \
-d yoshare/member-service:0.0.1-SNAPSHOT


# group-service
docker run --name group-service \
--privileged \
--network host \
-d yoshare/group-service:0.0.1-SNAPSHOT


