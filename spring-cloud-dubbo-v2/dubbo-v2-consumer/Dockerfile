# docker build -t dubbo-v2-consumer:1.0 .
# docker run -d -p 20302:20302 -p 20303:20303 --name=dubbo-v2-consumer dubbo-v2-consumer:1.0

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