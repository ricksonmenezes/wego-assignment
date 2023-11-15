FROM maven:3.6.3 AS maven
LABEL MAINTAINER="ricksonmenezes@gmail.com"

WORKDIR /usr/src/app

COPY . /usr/src/app
# Compile and package the application to an executable JAR
RUN mvn package -DskipTests

FROM openjdk:8-jdk-alpine

ARG JAR_FILE=assignment-0.0.1-SNAPSHOT.jar
#ADD target/assignment-0.0.1-SNAPSHOT.jar assignment.jar
WORKDIR /opt/app

COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/

EXPOSE 8080

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n","-Dspring.profiles.active=dev","-jar", "assignment-0.0.1-SNAPSHOT.jar"]