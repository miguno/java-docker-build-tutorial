# This justfile requires https://github.com/casey/just

# Load environment variables from `.env` file.
set dotenv-load

# print available targets
default:
    @just --list --justfile {{justfile()}}

# evaluate and print all just variables
evaluate:
    @just --evaluate

# print system information such as OS and architecture
system-info:
  @echo "architecture: {{arch()}}"
  @echo "os: {{os()}}"
  @echo "os family: {{os_family()}}"

# build the native application locally (requires GraalVM)
build-native:
    @./mvnw install -Dnative

# run the application locally (in Quarkus development mode) with hot reload
dev:
    @./mvnw quarkus:dev

# create a docker image (requires Docker)
docker-image-create:
    @echo "Creating a docker image ..."
    @./create_image.sh

# size of the docker image (requires Docker)
docker-image-size:
    @docker images $DOCKER_IMAGE_NAME

# run the docker image (requires Docker)
docker-image-run:
    @echo "Running container from docker image ..."
    @./start_container.sh

# clean (remove) the build artifacts
clean:
    @./mvnw clean

# package the application to create an uber jar
package:
    @./mvnw package

# run the application locally.
run:
    #!/usr/bin/env bash
    APP_JAR="target/app-runner.jar"
    if [ ! -f "$APP_JAR" ]; then
        just package
    else
        echo "Using existing application jar at $APP_JAR."
        echo "If you want to recompile, run \`./mvnw package\` (or \`just package\`) manually."
    fi
    java -jar "$APP_JAR"

# send request to the app's HTTP endpoint (requires running container)
send-request-to-app:
    curl http://localhost:8123/status

# run the test suite
test:
    @./mvnw test
