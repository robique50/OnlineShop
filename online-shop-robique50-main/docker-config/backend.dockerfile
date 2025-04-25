# Etapa de build: Folosim Maven pentru a compila aplicația Java
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Setăm directorul de lucru în container
WORKDIR /app

# Copiem fișierele pom.xml și src pentru a construi aplicația
COPY pom.xml .
COPY src ./src

# Construim aplicația și creăm JAR-ul (sărim peste teste pentru moment)
RUN mvn clean package -DskipTests

# Etapa de rulare: Folosim o imagine mai mică pentru a rula aplicația
FROM eclipse-temurin:17-jre

# Setăm directorul de lucru
WORKDIR /app

# Copiem JAR-ul compilat din etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expunem portul pe care rulează aplicația Spring Boot
EXPOSE 8080

# Comanda care va fi executată când containerul pornește
ENTRYPOINT ["java", "-jar", "app.jar"]