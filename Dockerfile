FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar 
COPY ${JAR_FILE} libreria.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/libreria.jar"]
