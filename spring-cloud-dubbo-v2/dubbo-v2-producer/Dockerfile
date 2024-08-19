# docker build -t dubbo-v2-producer:1.0 .
# docker run -d -p 20202:20202 -p 20203:20203 --name=dubbo-v2-producer -e JAVA_OPTS="--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED --add-opens=java.management/sun.management=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.math=ALL-UNNAMED" dubbo-v2-producer:1.0

# Use the OpenJDK 17.0.2 Oracle image
FROM openjdk:17.0.2-oracle

# Label the image
LABEL authors="roylic"

# JAR file location
ARG JAR_FILE=./target/*.jar

# Copy the JAR into the image
COPY ${JAR_FILE} app.jar

# Environment variable for JVM options
ENV JAVA_OPTS=""

# Set the entry point with the environment variable
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
