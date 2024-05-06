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

# audit the code
audit:
    #!/usr/bin/env bash
    echo "Running static code analysis with spotbugs"
    just spotbugs
    if command -v infer &>/dev/null; then
        echo "Running static code analysis with infer"
        just infer
    fi

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

# static code analysis via infer (requires https://github.com/facebook/infer)
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

# static code analysis with spotbugs
spotbugs: compile
    @./mvnw spotbugs:check

# run unit tests
test:
    @./mvnw test

# run integration tests (without unit tests)
test-integration:
    @./mvnw failsafe:integration-test

# upgrade mvnw a.k.a. maven wrapper
upgrade-mvnw:
    @./mvnw wrapper:wrapper

# run all tests, plus static code analysis with spotbugs
verify:
    @./mvnw verify

# same as 'verify', but for the native application (requires GraalVM)
verify-native:
    @./mvnw verify -Dnative

