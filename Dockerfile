FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/Libreria-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} Libreria-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Libreria-0.0.1-SNAPSHOT.jar"]