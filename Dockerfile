# Stage 1: Build the application
FROM eclipse-temurin:17-jdk AS build

WORKDIR /src

COPY . .

RUN chmod +x gradlew \
    && ./gradlew --no-daemon clean test bootWar

# Stage 2: Run the application
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build \
    /src/build/libs/coursehub-0.0.1-SNAPSHOT.war \
    /app/coursehub.war

RUN mkdir -p /app/data \
    && chown -R 10001:10001 /app

USER 10001:10001

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/coursehub.war"]
