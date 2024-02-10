# Build Stage
FROM openjdk:17-jdk-slim

# 폴더 위치
WORKDIR /app

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080
EXPOSE 3306
EXPOSE 27017

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "app.jar"]