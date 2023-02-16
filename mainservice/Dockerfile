FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar mainservice.jar
ENTRYPOINT ["java","-jar","/mainservice.jar"]