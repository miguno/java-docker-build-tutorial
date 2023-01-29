# Project Template: Create a Docker image for a Java application
[![Docker workflow status](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/docker-image.yml/badge.svg)](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/docker-image.yml)
[![Maven workflow status](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/maven.yml/badge.svg)](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/maven.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A template project to create a Docker image for a Java application.
The example application exposes an HTTP endpoint.

> **Golang developer?** Check out https://github.com/miguno/golang-docker-build-tutorial

Features:

* The Docker build uses a
  [multi-stage build setup](https://docs.docker.com/build/building/multi-stage/)
  to minimize the size of the generated Docker image, which is 176MB
* Supports [Docker BuildKit](https://docs.docker.com/build/)
* Java 17 (Eclipse Temurin)
* [JUnit 5](https://github.com/junit-team/junit5) for demonstrating how to integrate unit testing
* Maven for build management
* [GitHub Actions workflows](https://github.com/miguno/java-docker-build-tutorial/actions) for
  [Maven](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/maven.yml)
  and
  [Docker](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/docker-image.yml)
* Optionally, uses
  [just](https://github.com/casey/just)
  ![](https://img.shields.io/github/stars/casey/just)
  for running common commands conveniently, see [justfile](justfile).
* Uses [.env](.env) as central configuration to set variables used by
  [justfile](justfile) and other helper scripts in this project.

# Requirements

Docker must be installed on your local machine.  That's it.  You do not need a
Java JDK or Maven installed.

# Usage and Demo

**Step 1:** Create the Docker image according to [Dockerfile](Dockerfile).
This step uses Maven to build, test, and package the
[Java application](src/main/java/com/miguno/App.java) according to
[pom.xml](pom.xml).  The resulting image is 176MB in size, of which 170MB are
the underlying `eclipse-temurin` image.

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
miguno/java-docker-build-tutorial   latest    1403a608d055   4 minutes ago   176MB
```

**Step 2:** Start a container for the Docker image.

```shell
$ docker run -p 8123:8123 miguno/java-docker-build-tutorial:latest
```

**Step 3:** Open another terminal and access the example API endpoint of the
running container.

```shell
$ curl http://localhost:8123/status
{"status": "idle"}
```

# Usage with just

If you have [just](https://github.com/casey/just) installed, you can run the
commands above more conveniently as per this project's [justfile](justfile):

```shell
$ just
Available recipes:
    default             # print available targets
    docker-image-create # create a docker image (requires Docker)
    docker-image-run    # run the docker image (requires Docker)
    docker-image-size   # size of the docker image (requires Docker)
    evaluate            # evaluate and print all just variables
    send-request-to-app # send request to the app's HTTP endpoint (requires running container)
    system-info         # print system information such as OS and architecture
```

Example:

```shell
$ just docker-image-create
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
