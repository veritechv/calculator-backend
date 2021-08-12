# Base Alpine Linux based image with OpenJDK JRE only
FROM openjdk:8-jre-alpine

# expose api port
EXPOSE 8081

# copy application JAR (with libraries inside)
COPY target/calculator*.jar ./app.jar

# Start the REST services
# we specify the docker profile so we can use the postgres service name 
# to connect to the other container with postgres database
CMD ["/usr/bin/java", "-Dspring.profiles.active=docker", "-jar", "./app.jar"]
