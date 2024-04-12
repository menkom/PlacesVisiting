FROM bellsoft/liberica-openjdk-alpine:21.0.2 as builder
WORKDIR /app
COPY build.gradle gradlew settings.gradle ./
COPY gradle/ gradle/
COPY src/ src
RUN ./gradlew build --no-daemon -i --stacktrace  -x test

FROM bellsoft/liberica-openjdk-alpine:21.0.2
WORKDIR /opt/places-visiting
COPY --from=builder /app/build/libs/PlacesVisiting-0.0.1-SNAPSHOT.jar /opt/places-visiting/places-visiting.jar
CMD ["java", "-jar", "places-visiting.jar"]

EXPOSE 8080
