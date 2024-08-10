# oracle
FROM openjdk:17.0.2-oracle
# lael
LABEL authors="roylic"
# jar for building image
ARG JAR_FILE=./target/*.jar
# Copy the JAR into the image
COPY ${JAR_FILE} app.jar
# Set the entry point to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]