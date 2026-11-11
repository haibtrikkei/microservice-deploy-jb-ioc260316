# ==============================
# Build Stage
# ==============================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy gradle wrapper & configuration files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Fix CRLF line endings from Windows & set execute permission
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Download dependencies (cached layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code and build jar
COPY src ./src
RUN ./gradlew bootJar -x test --no-daemon

# ==============================
# Runtime Stage
# ==============================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Install curl for healthcheck
RUN apk add --no-cache curl

# Security: Run as non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built artifact
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8086

ENV JAVA_OPTS="-Xms128m -Xmx256m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

