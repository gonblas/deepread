# Dockerfile
FROM eclipse-temurin:17-jre

COPY backend/build/libs/my-app.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

# WORKDIR /app

# EXPOSE 8080
