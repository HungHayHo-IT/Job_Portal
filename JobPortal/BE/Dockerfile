
# ==========================
# Stage 1: Download dependencies
# ==========================
ARG BASE_JDK_IMAGE=eclipse-temurin:17-jdk-alpine
ARG BASE_JRE_IMAGE=eclipse-temurin:17-jre-alpine

FROM ${BASE_JDK_IMAGE} AS dependencies

RUN apk add --no-cache maven

WORKDIR /build

COPY pom.xml .

# Tải trước toàn bộ dependency để tận dụng Docker cache
RUN mvn dependency:go-offline

# ==========================
# Stage 2: Build application
# ==========================
FROM dependencies AS builder

WORKDIR /build

COPY src ./src

RUN mvn package -DskipTests

# ==========================
# Stage 3: Runtime
# ==========================
FROM ${BASE_JRE_IMAGE} AS runtime

WORKDIR /app

VOLUME ["/app/logs"]

ARG JAR_FILE=*.jar

COPY --from=builder /build/target/${JAR_FILE} app.jar

ARG IMAGE_VERSION=1.0.0

LABEL author="HungHayHo-IT" \
      org.opencontainers.image.version="${IMAGE_VERSION}" \
      org.opencontainers.image.title="Job Portal" \
      org.opencontainers.image.description="Spring Boot job portal "

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
