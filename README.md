# Project Template: Create a Docker image for a Java application

[![GitHub forks](https://img.shields.io/github/forks/miguno/java-docker-build-tutorial)](https://github.com/miguno/java-docker-build-tutorial/fork)
[![Docker workflow status](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/docker-image.yml/badge.svg)](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/docker-image.yml)
[![Maven workflow status](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/maven.yml/badge.svg)](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/maven.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A template project to create a Docker image for a Java application.
The example application uses Quarkus to expose an HTTP endpoint.

> **Golang developer?** Check out https://github.com/miguno/golang-docker-build-tutorial

Features:

- The Docker build uses a
  [multi-stage build setup](https://docs.docker.com/build/building/multi-stage/)
  including a downsized JRE (built inside Docker via `jlink`)
  to minimize the size of the generated Docker image, which is **117MB**.
- Supports [Docker BuildKit](https://docs.docker.com/build/)
- Supports GraalVM to create
  [native images](https://www.graalvm.org/latest/reference-manual/native-image/)
  (think: native binaries that do not require a JRE to be installed) for the
  example application. To keep things simple, the Docker setup of this project
  intentionally does not use these native app images because the majority of
  Java developers do not use GraalVM. If you do want to use native images,
  please modify [Dockerfile](Dockerfile) accordingly.
- Java 17 (Eclipse Temurin)
- [JUnit 5](https://github.com/junit-team/junit5) for demonstrating how to integrate unit testing
- Maven for build management, using [Maven Wrapper](https://github.com/apache/maven-wrapper)
- [GitHub Actions workflows](https://github.com/miguno/java-docker-build-tutorial/actions) for
  [Maven](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/maven.yml)
  and
  [Docker](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/docker-image.yml)
- Optionally, uses
  [just](https://github.com/casey/just)
  ![](https://img.shields.io/github/stars/casey/just)
  for running common commands conveniently, see [justfile](justfile).
- Uses [.env](.env) as central configuration to set variables used by
  [justfile](justfile) and other helper scripts in this project.

# Requirements

Docker must be installed on your local machine. That's it. You do not need a
Java JDK or Maven installed.

# Usage and Demo

**Step 1:** Create the Docker image according to [Dockerfile](Dockerfile).
This step uses Maven to build, test, and package the
[Java application](src/main/java/com/miguno/App.java) according to
[pom.xml](pom.xml). The resulting image is 102MB in size, of which 44MB are
the underlying `alpine` image.

```shell
# ***Creating an image may take a few minutes!***
$ docker build --platform linux/x86_64/v8 -t miguno/java-docker-build-tutorial:latest .

# You can also build with the new BuildKit.
# https://docs.docker.com/build/
$ docker buildx build --platform linux/x86_64/v8 -t miguno/java-docker-build-tutorial:latest .
```

Optionally, you can check the size of the generated Docker image:

```shell
$ docker images miguno/java-docker-build-tutorial
REPOSITORY                          TAG       IMAGE ID       CREATED         SIZE
miguno/java-docker-build-tutorial   latest    6eeb79c07157   4 minutes ago   102MB
```

**Step 2:** Start a container for the Docker image.

```shell
$ docker run -p 8123:8123 miguno/java-docker-build-tutorial:latest
```

**Step 3:** Open another terminal and access the example API endpoint of the
running container.

```shell
$ curl http://localhost:8123/status
{"status":"idle"}
```

# Usage with just

If you have [just](https://github.com/casey/just) installed, you can run the
commands above more conveniently as per this project's [justfile](justfile):

```shell
$ just
Available recipes:
    build-native        # build the native application locally (requires GraalVM)
    clean               # clean (remove) the build artifacts
    default             # print available targets
    dev                 # run the application locally (in Quarkus development mode) with hot reload
    docker-image-create # create a docker image (requires Docker)
    docker-image-run    # run the docker image (requires Docker)
    docker-image-size   # size of the docker image (requires Docker)
    evaluate            # evaluate and print all just variables
    package             # package the application to create an uber jar
    run                 # run the application locally.
    send-request-to-app # send request to the app's HTTP endpoint (requires running container)
    system-info         # print system information such as OS and architecture
    test                # run the test suite
```

Example:

```shell
$ just docker-image-create
```

# Notes

You can also build, test, package, and run the Java application locally
(without Docker) if you have JDK 17+ installed. You do not need to have Maven
installed, because this repository contains the
[Maven Wrapper](https://github.com/apache/maven-wrapper) `mvnw` (use `mvnw.cmd`
on Windows).

```shell
# Build, test, package the application locally.
$ ./mvnw clean package

# Run the application locally.
$ java -jar target/app-runner.jar

# Alternatively, you can run the application in development mode with hot reloading.
$ ./mvnw quarkus:dev
```

# References

- [How to reduce Java Docker image size](https://blog.monosoul.dev/2022/04/25/reduce-java-docker-image-size/)
  (with `jlink`)
- [Creating your own runtime using jlink](https://adoptium.net/blog/2021/10/jlink-to-produce-own-runtime/)
- [Using Jlink in Dockerfiles instead of a JRE](https://adoptium.net/blog/2021/08/using-jlink-in-dockerfiles/)
