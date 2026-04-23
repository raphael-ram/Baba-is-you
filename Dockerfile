# ── Stage 1 : build Maven ────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Dépendances en cache séparément du code source
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -q -DskipTests

# ── Stage 2 : image runtime minimale ─────────────────────────────────────────
FROM eclipse-temurin:25-jre
RUN apt-get update && apt-get install -y --no-install-recommends wget && rm -rf /var/lib/apt/lists/*
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
