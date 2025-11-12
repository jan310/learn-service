FROM amazoncorretto:25.0.1
COPY build/libs/learn-service-0.0.1-SNAPSHOT.jar learn-service.jar
ENTRYPOINT ["java", "-jar", "learn-service.jar"]