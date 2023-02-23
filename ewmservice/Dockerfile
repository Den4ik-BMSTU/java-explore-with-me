FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar ewmservice.jar
ENTRYPOINT ["java","-jar","/ewmservice.jar"]