# Build the image parking-event-generator
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ./target/parking-event-generator-0.0.1-SNAPSHOT.jar parking-event-generator.jar
ENV CONFIG_SERVER_HOST=config-server
ENTRYPOINT ["java", "-jar", "/app/parking-event-generator.jar"]