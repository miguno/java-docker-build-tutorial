# Multi-stage build setup (https://docs.docker.com/develop/develop-images/multistage-build/)

# Stage 1 (to create a "build" image, ~140MB)
FROM eclipse-temurin:17-jdk-alpine AS builder
# smoke test to verify if java is available
RUN java -version

COPY . /usr/src/myapp/
WORKDIR /usr/src/myapp/
RUN set -Eeux \
    && apk update \
    && apk upgrade \
    && apk --no-cache add maven \
    # smoke test to verify if maven is available
    && mvn --version
RUN mvn package

# Stage 2 (to create a downsized "container executable", ~87MB)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /root/
COPY --from=builder /usr/src/myapp/target/app.jar .

EXPOSE 8123
ENTRYPOINT ["java", "-jar", "./app.jar"]
