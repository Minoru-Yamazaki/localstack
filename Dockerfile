#FROM ubuntu:latest
#LABEL authors="minor"
#
#ENTRYPOINT ["top", "-b"]

# Etapa 1: build do JAR com Maven
FROM maven:3.9-eclipse-temurin-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagem leve para rodar o app
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
