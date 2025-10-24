# Build stage
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copier les fichiers nécessaires
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Télécharger wait-for-it.sh
RUN apt-get update && apt-get install -y curl \
    && curl -L -o wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh \
    && chmod +x wait-for-it.sh

# Préparer le build
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY .env .env
COPY src src
RUN ./mvnw clean package -U
RUN mv target/*.jar app.jar

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app

# Copier le jar ET wait-for-it.sh depuis le build
COPY --from=build /app/app.jar app.jar
COPY --from=build /app/wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--timeout=30", "--strict", "--", "java", "-jar", "app.jar"]
