FROM openjdk:17-alpine
ADD target/*.jar app.jar

EXPOSE 8999

ENTRYPOINT [ "sh", "-c", "java \
    -Dspring.profiles.active=prod \
    -jar /app.jar \
    "]
