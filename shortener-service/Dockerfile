# Day 9+: multi-stage build for Railway. Stub kept for monorepo layout.
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/shortener-service-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
