# Project Template: Create a Docker image for a Java application
[![Docker workflow status](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/docker-image.yml/badge.svg)](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/docker-image.yml)
[![Maven workflow status](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/maven.yml/badge.svg)](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/maven.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A template project to create a Docker image for a Java application.
The example application exposes an HTTP endpoint.

> **Golang developer?** Check out https://github.com/miguno/golang-docker-build-tutorial

Features:

* The Docker build uses a
  [multi-stage build setup](https://docs.docker.com/develop/develop-images/multistage-build/)
  to minimize the size of the generated Docker image.
* Java 17 (Eclipse Temurin)
* Junit 5 for demonstrating how to integrate unit testing
* Maven for build management

# Requirements

Docker must be installed on your local machine.  That's it.  You do not need a
Java JDK or Maven installed.

# Usage and Demo

**Step 1:** Create the Docker image according to [Dockerfile](Dockerfile).
This step uses Maven to build, test, and package the
[Java application](src/main/java/com/miguno/App.java) according to
[pom.xml](pom.xml).  The resulting image is 364MB in size.

```shell
# Alternatively, run `./create_image.sh`.
# ***Creating an image may take a few minutes!***
$ docker build --platform linux/x86_64/v8 -t miguno/java-docker-build-tutorial:latest .

# You can also build with the new BuildKit.
# https://docs.docker.com/build/
$ docker buildx build --platform linux/x86_64/v8 -t miguno/java-docker-build-tutorial:latest .
```

**Step 2:** Start a container for the Docker image.

```shell
# Alternatively, run `./start_container.sh`.
$ docker run -p 8123:8123 miguno/java-docker-build-tutorial:latest
```

**Step 3:** Open another terminal and access the example API endpoint.

```shell
$ curl http://localhost:8123/status
{"status": "idle"}
```

# Notes

You can also build, test, package, and run the Java application locally
(without Docker) if you have JDK 17+ and Maven installed.

```shell
# Build, test, package the application locally
$ mvn clean package

# Run the example application locally
$ java -jar target/app.jar
```
