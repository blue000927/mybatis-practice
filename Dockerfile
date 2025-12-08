FROM eclipse-temurin:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", \
            "-XX:-UseContainerSupport", \
            "-jar", \
            "-Dspring.profiles.active=stress", \
            "/app.jar"]