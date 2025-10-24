# Étape 1 : build Maven
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copier uniquement le pom pour cache des dépendances
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Télécharger les dépendances avant de copier le code source
RUN ./mvnw dependency:resolve

# Copier le code source
COPY src src

# Build
RUN ./mvnw clean package -U

# Étape 2 : runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
