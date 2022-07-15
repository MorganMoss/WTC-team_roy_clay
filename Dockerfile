# DOCKERFILE - creating a Maven Builder Docker Image
# Start by creating the base image for the container
# We will use alpine-linux
# ----
FROM debian:buster-slim

RUN apt-get update
RUN apt-get install -y openjdk-11-jre curl

WORKDIR /app

ADD libs/Server-1.0.3-SNAPSHOT.jar /app

# Create project server jar variable, expose port and define base commands
EXPOSE 5050
CMD ["java", "-jar", "Server-1.0.3-SNAPSHOT.jar"]