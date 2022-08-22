FROM openjdk:11-jre-slim

LABEL maintainer="Playground"

WORKDIR /app
RUN apt-get update

# COPY rds-truststore.jks rds-truststore.jks
RUN ls && pwd

COPY target/airtableDB-0.0.1-SNAPSHOT.jar /opt/Playground-video-backend.jar
RUN ls && pwd
RUN md5sum /opt/Playground-video-backend.jar

ENTRYPOINT java -jar /opt/Playground-video-backend.jar
# -DsslKeyStore=/app/rds-truststore.jks