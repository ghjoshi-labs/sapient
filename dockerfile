FROM openjdk:8-jdk-alpine
WORKDIR /weather
COPY ./target/weather-0.0.1.jar /weather

EXPOSE 8080
CMD ["java", "-jar", "weather-0.0.1.jar"]