FROM gradle:8.6-jdk21-alpine AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew ./

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew bootJar -x test --no-daemon


FROM eclipse-temurin:21-jre-alpine

USER root

RUN apk add --no-cache ca-certificates && update-ca-certificates

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

RUN chown spring:spring app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]