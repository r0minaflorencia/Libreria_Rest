# Etapa 1: Build con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /libreria
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final con OpenJDK
FROM openjdk:21-jdk-slim
WORKDIR /libreria
COPY --from=build /libreria/target/*.jar Libreria-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Libreria-0.0.1-SNAPSHOT.jar"]

