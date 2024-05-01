# syntax=docker/dockerfile:1

# We use a multi-stage build setup.
# (https://docs.docker.com/build/building/multi-stage/)

###############################################################################
# Stage 1 (to create a "build" image, ~360MB)                                 #
###############################################################################
FROM eclipse-temurin:17-jdk-alpine AS builder

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
RUN set -Eeux \
    && apk --no-cache add maven \
    # Smoke test to verify if maven is available.
    && mvn --version
RUN mvn package

###############################################################################
# Stage 2 (to create a downsized "container executable", ~102MB)              #
###############################################################################
FROM alpine:latest
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
RUN apk --no-cache add ca-certificates

# Add app user.
ARG APP_USER=appuser
ARG APP_USER_ID=1000
ARG APP_GROUP=apps
ARG APP_GROUP_ID=1000
RUN addgroup --gid $APP_GROUP_ID $APP_GROUP
RUN adduser --no-create-home --disabled-password --ingroup $APP_GROUP --uid $APP_USER_ID $APP_USER

# Configure work directory.
ARG APP_DIR=/app
RUN mkdir $APP_DIR && \
    chown -R $APP_USER:$APP_GROUP $APP_DIR
WORKDIR $APP_DIR

# Copy downsized JRE from builder image.
COPY --from=builder /minimal-jre $JAVA_HOME

# Copy packaged app from builder image.
COPY --from=builder --chown=$APP_USER:$APP_GROUP /usr/src/myapp/target/app.jar .

USER $APP_USER:$APP_GROUP
EXPOSE 8123
ENTRYPOINT ["java", "-jar", "./app.jar"]
