FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar statservice.jar
ENTRYPOINT ["java","-jar","/statservice.jar"]