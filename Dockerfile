FROM openjdk:8-jdk-alpine
ADD target/assignment-0.0.1-SNAPSHOT.jar assignment.jar
EXPOSE 8080
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n", "-jar", "assignment.jar"]