# Stage 1: build the jar
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew bootJar --no-daemon

#Stage 2: run the jar
FROM eclipse-temurin:25-jre
WORKDIR /app

RUN useradd --system --create-home lime
COPY --from=build /app/build/libs/*.jar app.jar
USER lime

EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=60", "-jar", "app.jar"]