FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/manager-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=dev","-server","-d64","-Xms512m","-Xmx512m","-Xmn256m","-Xss512k","/app.jar"]