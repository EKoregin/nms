FROM maven:3.6.3-openjdk-17
RUN mkdir nms
WORKDIR nms
COPY . .
RUN mvn package -Dmaven.test.skip=true
EXPOSE 8999
CMD ["java", "-jar", "target/nms-0.0.1.jar"]