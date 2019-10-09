FROM openjdk:8u212-jdk-slim
LABEL maintainer="shoebahmed@yahoo.com"
VOLUME /tmp
EXPOSE 8081
ADD target/gfg-assignment-0.0.1-SNAPSHOT.jar gfg-assignment.jar
ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongo/gfg","-Dspring.profiles.active=stagingData","-jar","/gfg-assignment.jar"]