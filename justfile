# This justfile requires https://github.com/casey/just

# Load environment variables from `.env` file.
set dotenv-load

project_dir := justfile_directory()
build_dir := project_dir + "/target"
app_uber_jar := build_dir + "/app-runner.jar"
app_native_image := build_dir + "/app-runner"

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
    @echo "Producing a native app image via GraalVM ..."
    @echo "See https://quarkus.io/guides/building-native-image#configuring-graalvm"
    @./mvnw install -Dnative && echo "The native app image was successfully created at: {{app_native_image}}"

# run the application locally (in Quarkus development mode) with live reload
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

# compile the project
compile:
    @./mvnw compile

# clean (remove) the build artifacts
clean:
    @./mvnw clean

# run static analysis via infer (requires https://github.com/facebook/infer)
infer:
    @infer run -- ./mvnw clean compile

# package the application to create an uber jar
package:
    @./mvnw package

# run the application locally
run:
    #!/usr/bin/env bash
    APP_JAR="{{app_uber_jar}}"
    if [ ! -f "$APP_JAR" ]; then
        just package
    else
        echo "Using existing application uber jar at $APP_JAR."
        echo "If you want to recompile the uber jar, run \`./mvnw package\` (or \`just package\`) manually."
    fi
    java -jar "$APP_JAR"

# run the native application locally (requires GraalVM)
run-native:
    #!/usr/bin/env bash
    APP_BINARY="{{app_native_image}}"
    if [ ! -f "$APP_BINARY" ]; then
        just build-native
    else
        echo "Using existing application native image at $APP_BINARY."
        echo "If you want to recompile the native image, run \`./mvnw install -Dnative\` (or \`just build-native\`)."
    fi
    "$APP_BINARY"

# generate site incl. reports for spotbugs, dependencies, licenses
site: compile
    @./mvnw site && echo "Reports are available under {{build_dir}}/site/"

# send request to the app's HTTP endpoint (requires Docker and running app container)
send-request-to-app:
    curl http://localhost:8123/status

# run spotbugs checks
spotbugs: compile
    @./mvnw spotbugs:check

# run the test suite
test:
    @./mvnw test

# run the test suite for the native application (requires GraalVM)
test-native:
    @./mvnw verify -Dnative

# verify the project with maven's verify target
verify:
    @./mvnw verify
