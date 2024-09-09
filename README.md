# Project Template: Create a Docker image for a Java application

[![GitHub forks](https://img.shields.io/github/forks/miguno/java-docker-build-tutorial)](https://github.com/miguno/java-docker-build-tutorial/fork)
[![Docker workflow status](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/docker-image.yml/badge.svg)](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/docker-image.yml)
[![Maven workflow status](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/maven.yml/badge.svg)](https://github.com/miguno/java-docker-build-tutorial/actions/workflows/maven.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A template project to create a Docker image for a Java application.
The [example application](src/main/java/com/miguno/javadockerbuild/App.java)
uses Spring Boot to expose an HTTP endpoint at
[`/welcome`](http://localhost:8123/welcome).

> **Golang developer?** Check out https://github.com/miguno/golang-docker-build-tutorial

Features:

- The Docker build uses a
  [multi-stage build setup](https://docs.docker.com/build/building/multi-stage/)
  including a downsized JRE (built inside Docker via `jlink`)
  to minimize the size of the generated Docker image, which is **157MB**.
- Supports [Docker BuildKit](https://docs.docker.com/build/)
- Java 22 (Eclipse Temurin)
- [JUnit 5](https://github.com/junit-team/junit5) for testing,
  [Jacoco](https://github.com/jacoco/jacoco) for code coverage,
  [SpotBugs](https://github.com/spotbugs/spotbugs) for static code analysis
- Swagger UI and OpenAPI v3 integration via [springdoc](https://springdoc.org/)
  at endpoints [/swagger-ui.html](http://localhost:8123/swagger-ui.html) and
  and [/v3/api-docs](http://localhost:8123/v3/api-docs)
- [Spring Actuator](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)
  at endpoint [/actuator](http://localhost:8123/actuator), e.g. for
  [healthchecks](http://localhost:8123/actuator/health) or [Prometheus
  metrics](http://localhost:8123/actuator/prometheus)
- Integrates [Spring Boot
  Admin](https://github.com/codecentric/spring-boot-admin) at endpoint
  [`/admin`](http://localhost:8123/admin) to inspect the running application.
  Login with username `admin` and password `admin`.<br />
  <a href="https://github.com/miguno/java-docker-build-tutorial/raw/main/images/spring-boot-admin-dashboard.png"><img src="https://github.com/miguno/java-docker-build-tutorial/raw/main/images/spring-boot-admin-dashboard.png" alt="Spring Boot Admin screenshot" width="300"></a><br />
  Note that, in production, [it is recommended to
  separate the SBA server](https://docs.spring-boot-admin.com/current/faq.html)
  from your application (the SBA client), unlike what this project does for
  demonstration purposes.
- Maven for build management (see [pom.xml](pom.xml)), using
  [Maven Wrapper](https://github.com/apache/maven-wrapper)
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
This step uses Maven to build, test, and package the Java application according
to [pom.xml](pom.xml). The resulting image is 157MB in size, of which 44MB are
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
miguno/java-docker-build-tutorial   latest    bd64d898a04e   2 minutes ago   131MB
```

**Step 2:** Start a container for the Docker image.

```shell
$ docker run -p 8123:8123 miguno/java-docker-build-tutorial:latest
```

<details>
  <summary>Example output (click to expand)</summary>

```
Running container from docker image ...
Starting container for image 'miguno/java-docker-build-tutorial:latest', exposing port 8123/tcp
- Run 'curl http://localhost:8123/welcome' to send a test request to the containerized app.
- Enter Ctrl-C to stop the container.

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.3.3)

2024-08-26T15:45:08.859Z  INFO 1 --- [main] com.miguno.javadockerbuild.App           : Starting App v1.0.0-SNAPSHOT using Java 22.0.2 with PID 1 (/app/app.jar started by appuser in /app)
2024-08-26T15:45:08.868Z  INFO 1 --- [main] com.miguno.javadockerbuild.App           : No active profile set, falling back to 1 default profile: "default"
2024-08-26T15:45:10.930Z  INFO 1 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8123 (http)
2024-08-26T15:45:10.950Z  INFO 1 --- [main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-08-26T15:45:10.951Z  INFO 1 --- [main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.28]
2024-08-26T15:45:10.991Z  INFO 1 --- [main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-08-26T15:45:10.992Z  INFO 1 --- [main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2004 ms
2024-08-26T15:45:12.452Z  INFO 1 --- [main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint beneath base path '/actuator'
2024-08-26T15:45:12.562Z  INFO 1 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8123 (http) with context path '/'
2024-08-26T15:45:12.597Z  INFO 1 --- [main] com.miguno.javadockerbuild.App           : Started App in 5.0 seconds (process running for 6.246)
```

</details>

**Step 3:** Open another terminal and access the example API endpoint of the
running container.

```shell
$ curl http://localhost:8123/welcome
{"welcome":"Hello, World!"}
```

# Local usage without Docker

You can also build, test, package, and run the Java application locally
(without Docker) if you have JDK 22+ installed. You do not need to have Maven
installed, because this repository contains the
[Maven Wrapper](https://github.com/apache/maven-wrapper) `mvnw` (use `mvnw.cmd`
on Windows).

```shell
# Build, test, package the application locally.
$ ./mvnw clean verify package

# Run the application locally.
$ java -jar target/app.jar

# Alternatively, run the application locally via Spring Boot's maven targets.
$ ./mvnw spring-boot:run
```

# Appendix: Usage with just

If you have [just](https://github.com/casey/just) installed, you can run the
commands above more conveniently as per this project's [justfile](justfile):

```shell
$ just
Available recipes:
    audit               # audit the code
    benchmark-plow      # benchmark the app's HTTP endpoint with plow (requires https://github.com/six-ddc/plow)
    benchmark-wrk       # benchmark the app's HTTP endpoint with wrk (requires https://github.com/wg/wrk)
    build               # alias for 'compile'
    clean               # clean (remove) the build artifacts
    compile             # compile the project
    coverage            # create coverage report
    default             # print available targets
    docker-image-create # create a docker image (requires Docker)
    docker-image-run    # run the docker image (requires Docker)
    docker-image-size   # size of the docker image (requires Docker)
    docs                # generate Java documentation
    evaluate            # evaluate and print all just variables
    format              # format sources
    format-check        # check formatting of sources (without modifying)
    infer               # static code analysis with infer (requires https://github.com/facebook/infer)
    mvnw-upgrade        # upgrade mvnw a.k.a. maven wrapper
    package             # package the application to create an uber jar
    pom                 # print effective pom.xml
    run                 # run the application locally with live reload
    run-jar             # run the application's packaged jar locally (requires 'package' step)
    send-request-to-app # send request to the app's HTTP endpoint (requires Docker and running app container)
    site                # generate site incl. reports for spotbugs, dependencies, javadocs, licenses
    spotbugs            # static code analysis with spotbugs
    system-info         # print system information such as OS and architecture
    test                # run unit tests
    verify              # run unit and integration tests, plus coverage check and static code analysis
```

Example:

```shell
$ just docker-image-create
```

# References

- [How to reduce Java Docker image size](https://blog.monosoul.dev/2022/04/25/reduce-java-docker-image-size/)
  (with `jlink`)
- [Creating your own runtime using jlink](https://adoptium.net/blog/2021/10/jlink-to-produce-own-runtime/)
- [Using Jlink in Dockerfiles instead of a JRE](https://adoptium.net/blog/2021/08/using-jlink-in-dockerfiles/)
