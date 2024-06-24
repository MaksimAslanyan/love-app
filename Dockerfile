FROM openjdk:17-jdk
RUN mkdir /app
RUN mkdir -p /var/logs
COPY target/dating-user-api-0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app
EXPOSE 8086 8887
ENTRYPOINT ["java", "-jar", "app.jar"]

