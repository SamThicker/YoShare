#!/bin/bash

# eureka-service
#nohup java -jar "-Dspring.profiles.active=dev" ./eureka-server-0.0.1-SNAPSHOT.jar > $./eureka-server.info 2>&1 &
nohup java -Xmx128m -Xms64m -jar ./eureka-server-0.0.1-SNAPSHOT.jar > ./eureka-server.log 2>&1 &

# zuul-service
nohup java -Xmx128m -Xms64m -jar ./zuul-service-0.0.1-SNAPSHOT.jar > ./zuul-service.log 2>&1 &

# security-service
nohup java -Xmx128m -Xms64m -jar ./security-service-0.0.1-SNAPSHOT.jar > ./security-service.log 2>&1 &

# member-service
nohup java -Xmx128m -Xms64m -jar ./member-service-0.0.1-SNAPSHOT.jar > ./member-service.log 2>&1 &

# group-service
nohup java -Xmx128m -Xms64m -jar ./group-service-0.0.1-SNAPSHOT.jar > ./group-service.log 2>&1 &


