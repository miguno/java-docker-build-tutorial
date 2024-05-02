# syntax=docker/dockerfile:1

# We use a multi-stage build setup.
# (https://docs.docker.com/build/building/multi-stage/)

###############################################################################
# Stage 1 (to create a "build" image)                                         #
###############################################################################
FROM eclipse-temurin:21-jdk-alpine AS builder

# Smoke test to verify if java is available.
RUN java -version

### Build a downsized JRE
# Required for jlink's `--strip-debug` option.
RUN apk add --no-cache binutils
RUN jlink \
    --verbose \
    --add-modules ALL-MODULE-PATH \
    --compress=2 \
    --no-header-files \
    --no-man-pages \
    --strip-debug \
    --output /minimal-jre

# Build and package the app.
COPY . /usr/src/myapp/
WORKDIR /usr/src/myapp/
RUN ./mvnw package

###############################################################################
# Stage 2 (to create a downsized "container executable", ~131MB)              #
###############################################################################
FROM alpine:latest
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
RUN apk --no-cache add ca-certificates

# Add app user.
ARG USER_NAME="appuser"
ARG USER_ID="1000"
ARG GROUP_NAME="apps"
ARG GROUP_ID="1000"
RUN addgroup --gid $GROUP_ID $GROUP_NAME && \
    adduser --no-create-home --disabled-password --ingroup $GROUP_NAME --uid $USER_ID $USER_NAME

# Configure work directory.
ARG APP_DIR=/app
RUN mkdir $APP_DIR && \
    chown -R $USER_NAME:$GROUP_NAME $APP_DIR
WORKDIR $APP_DIR

# Copy downsized JRE from builder image.
COPY --from=builder /minimal-jre $JAVA_HOME

# Copy packaged app from builder image.
COPY --from=builder --chown=$USER_NAME:$GROUP_NAME /usr/src/myapp/target/app-runner.jar ./app.jar

# Run the application.
USER $USER_NAME:$GROUP_NAME
EXPOSE 8123
ENTRYPOINT ["java", "-jar", "./app.jar"]
