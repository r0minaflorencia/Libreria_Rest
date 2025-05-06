FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/libreria_delta-0.0.1.jar 
COPY ${JAR_FILE} app_libreria.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_libreria.jar"]
