# ─── STAGE 1: Build with Maven (include spring-boot:repackage) ─────────────
FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /build

# 1) Copy the parent POM and the api-service folder (with its own POM + src)
COPY pom.xml .
COPY api-service ./api-service

# 2) Build & repackage ONLY the api-service module (and any modules it depends on)
#    Using “-pl api-service -am spring-boot:repackage” forces Maven to create a fat jar.
RUN mvn -f ./pom.xml clean package \
      -DskipTests \
      -pl api-service \
      -am \
      spring-boot:repackage


# ─── STAGE 2: Copy the resulting fat-jar into a lightweight JRE ──────────────
FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR /app

# 3) Copy exactly the “repackaged” Spring Boot JAR built by stage 1
COPY --from=builder /build/api-service/target/*.jar ./app.jar

# 4) Expose port 8080 (matches your “docker” profile)
EXPOSE 8080

# 5) Run the fat jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
