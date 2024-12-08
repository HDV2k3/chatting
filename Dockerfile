# Stage 1: build
# Start with a Maven image that includes JDK 21
FROM maven:3.9.8-amazoncorretto-21 AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source code with Maven
RUN mvn package -DskipTests

# Stage 2: create image
# Start with Amazon Corretto JDK 21
FROM amazoncorretto:21.0.4


# Set working folder to /app and copy compiled file from the build stage
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app will run on
EXPOSE 8082

# Command to run the application, with custom JVM options or environment variables if needed
ENTRYPOINT ["java", "-Dlogging.level.org.apache.coyote.http11=DEBUG", "-jar", "app.jar", "--server.port=8082"]
