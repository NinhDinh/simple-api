FROM eclipse-temurin:25-jre
WORKDIR /opt/app

RUN groupadd --system --gid 10001 spring && \
    useradd --system --uid 10001 --gid 10001 --create-home --home-dir /home/spring spring

ARG JAR_FILE=target/*.jar
COPY --chown=10001:10001 ${JAR_FILE} /opt/app/app.jar

USER 10001:10001

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
