FROM openjdk:8-jre-slim

COPY target/expert-file.jar /app/expert-file.jar

CMD ["java", "-jar", "/app/expert-file.jar"]
