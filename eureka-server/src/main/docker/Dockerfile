FROM java:8

ADD /eureka-server-0.0.1-SNAPSHOT.jar /

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod","/eureka-server-0.0.1-SNAPSHOT.jar"]
