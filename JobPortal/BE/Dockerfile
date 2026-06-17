# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Copy file cấu hình maven và pom.xml
COPY pom.xml .
# Copy mã nguồn
COPY src ./src
# Build project bỏ qua test
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy file jar từ stage build
COPY --from=build /app/target/BE-0.0.1-SNAPSHOT.jar app.jar
# Mở port backend
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]