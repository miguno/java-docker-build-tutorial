# Tutorial: Create a Docker image for a Java application

A template project to create a Docker image for a Java application.
The example application exposes an HTTP endpoint.

The Docker build uses a [multi-stage build setup](https://docs.docker.com/develop/develop-images/multistage-build/)
to minimize the size of the generated Docker image.


# Requirements

Docker must be installed. That's it. You do not need a Java JDK or Maven.


# Usage and Demo

**Step 1:** Create the Docker image `miguno/java-docker-build:latest` according to [Dockerfile](Dockerfile).
This step uses Maven to build, test, and package the Java application according to [pom.xml](pom.xml).
The resulting image is 87MB in size.

```shell
# This may take a few minutes.
$ ./build_image.sh
```

> Example output:
>
> ```
> Building image 'miguno/java-docker-build:latest'
> ...
> Successfully tagged miguno/java-docker-build:latest
> ```

**Step 2:** Start a container for the Docker image.

```shell
$ ./run_image.sh
```

> Example output:
>
> ```
> Starting container for image 'miguno/java-docker-build:latest', exposing port 8123/tcp
> ...
> Endpoint is available at http://0.0.0.0:8123/status
> ```

**Step 3:** Open another terminal and access the example API endpoint.

```shell
$ curl http://localhost:8123/status
{"status": "idle"}
```


# Notes

You can also build, test, and package the Java application locally (without Docker)
if you have JDK 8+ and Maven installed.

```shell
$ mvn package
```
