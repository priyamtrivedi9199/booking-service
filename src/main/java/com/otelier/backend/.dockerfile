FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy maven files for dependency caching
COPY ../../../../../../pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Run the application
EXPOSE 8080
CMD ["java", "-jar", "target/backend-1.0.0.jar"]
