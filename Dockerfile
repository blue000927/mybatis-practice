FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", \
            "-XX:-UseContainerSupport", \
            "-Dmanagement.metrics.enable.process.cpu=false", \
            "-Dmanagement.metrics.enable.system.cpu=false", \
            "-jar", \
            "-Dspring.profiles.active=stress", \
            "/app.jar"]