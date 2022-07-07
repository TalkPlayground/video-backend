FROM openjdk:11-jre-slim

LABEL maintainer="Playground"
 
RUN apt-get update

COPY target/Playground-video-backend-0.0.1-SNAPSHOT.jar /opt/Playground-video-backend.jar

RUN md5sum /opt/Playground-video-backend.jar

ENTRYPOINT java -jar /opt/Playground-video-backend.jar
