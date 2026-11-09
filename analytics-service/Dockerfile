# Day 9+: multi-stage build for Railway. Stub kept for monorepo layout.
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/analytics-service-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
