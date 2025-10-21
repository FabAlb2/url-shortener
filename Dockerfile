# 1️⃣ Build-Stage: baut das Spring Boot JAR mit Gradle
FROM gradle:8.7-jdk17 AS builder
WORKDIR /home/gradle/project

# Projekt in das Image kopieren (als gradle-User, damit Cache klappt)
COPY --chown=gradle:gradle . .

# Build (ohne Daemon für reproduzierbare CI-Builds)
RUN gradle clean bootJar --no-daemon

# 2️⃣ Runtime-Stage: nur das fertige JAR, schlankes JRE-Image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Jar aus der Build-Stage holen
COPY --from=builder /home/gradle/project/build/libs/*.jar /app/app.jar

# Spring Boot hört standardmäßig auf 8080
EXPOSE 8080

# Startkommando
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
