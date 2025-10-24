# Build stage
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn

RUN apt-get update && apt-get install -y curl \
    && curl -L -o wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh \
    && chmod +x wait-for-it.sh

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY .env .env
COPY src src
RUN ./mvnw clean package -U
RUN mv target/*.jar app.jar

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copier le jar et le script
COPY --from=build /app/app.jar app.jar
COPY --from=build /app/wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

# Ne pas changer d'utilisateur (on reste root)
EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--timeout=30", "--strict", "--", "java", "-jar", "app.jar"]
