# This justfile requires https://github.com/casey/just

# Load environment variables from `.env` file.
set dotenv-load
# Fail the script if the env file is not found.
set dotenv-required

project_dir := justfile_directory()
build_dir := project_dir + "/target"
app_uber_jar := build_dir + "/app.jar"

# print available targets
[group("project-agnostic")]
default:
    @just --list --justfile {{justfile()}}

# evaluate and print all just variables
[group("project-agnostic")]
evaluate:
    @just --evaluate

# print system information such as OS and architecture
[group("project-agnostic")]
system-info:
  @echo "architecture: {{arch()}}"
  @echo "os: {{os()}}"
  @echo "os family: {{os_family()}}"

# perform static code analysis
[group("development")]
analyze:
    #!/usr/bin/env bash
    echo "Running static code analysis with spotbugs"
    just spotbugs
    if command -v infer &>/dev/null; then
        echo "Running static code analysis with infer"
        just infer
    fi

# benchmark the app's HTTP endpoint with plow (requires https://github.com/six-ddc/plow)
[group("benchmarking")]
benchmark-plow:
    @echo plow -c 100 --duration=30s http://localhost:${APP_PORT}/welcome
    @plow      -c 100 --duration=30s http://localhost:${APP_PORT}/welcome

# benchmark the app's HTTP endpoint with wrk (requires https://github.com/wg/wrk)
[group("benchmarking")]
benchmark-wrk:
    @echo wrk -t 10 -c 100 --latency --duration 30 http://localhost:${APP_PORT}/welcome
    @wrk      -t 10 -c 100 --latency --duration 30 http://localhost:${APP_PORT}/welcome

# alias for 'compile'
[group("development")]
build: compile

# clean (remove) the build artifacts
[group("development")]
clean:
    @./mvnw clean

# compile the project
[group("development")]
compile:
    @./mvnw compile

# create coverage report
[group("development")]
coverage: verify
    @./mvnw jacoco:report && \
        echo "Coverage report is available under {{build_dir}}/site/jacoco/"

# list dependency tree of this project
[group("development")]
dependencies:
    @./mvnw dependency:tree

# create a docker image (requires Docker)
[group("docker")]
docker-image-create:
    @echo "Creating a docker image ..."
    @./tools/create_image.sh

# size of the docker image (requires Docker)
[group("docker")]
docker-image-size:
    @docker images $DOCKER_IMAGE_NAME

# run the docker image (requires Docker)
[group("docker")]
docker-image-run:
    @echo "Running container from docker image ..."
    @./tools/start_container.sh

# generate Java documentation
[group("development")]
docs:
    @./mvnw javadoc:javadoc

# format sources
[group("development")]
format:
    @./mvnw spotless:apply

# check formatting of sources (without modifying)
[group("development")]
format-check:
    @./mvnw spotless:check

# static code analysis with infer (requires https://github.com/facebook/infer)
[group("development")]
infer:
    @infer run -- ./mvnw clean compile

# list active profiles
[group("maven")]
maven-active-profiles:
    @./mvnw help:active-profiles

# list all profiles
[group("maven")]
maven-all-profiles:
    @./mvnw help:all-profiles

# show maven lifecycles like 'clean', 'compile'
[group("maven")]
maven-lifecycles:
    @echo "See https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference"

# print effective pom.xml
[group("maven")]
maven-pom:
    @./mvnw help:effective-pom

# show help of maven-help-plugin
[group("maven")]
maven-help:
    @./mvnw help:help

# print platform details like system properties, env variables
[group("maven")]
maven-system:
    @./mvnw help:system

# upgrade maven wrapper
[group("maven")]
mvnw-upgrade:
    @./mvnw wrapper:wrapper

# list outdated dependencies
[group("development")]
outdated:
    @./mvnw versions:display-dependency-updates

# list outdated maven plugins
[group("development")]
outdated-plugins:
    @./mvnw versions:display-plugin-updates

# package the app to create an uber jar
[group("development")]
package:
    @./mvnw verify package

# send request to the app's HTTP endpoint (requires running app)
[group("development")]
send-request-to-app:
    @echo curl http://localhost:${APP_PORT}/welcome
    @curl      http://localhost:${APP_PORT}/welcome

# generate site incl. reports for spotbugs, dependencies, javadocs, licenses
[group("development")]
site: compile
    @./mvnw site && \
        echo "Reports are available under {{build_dir}}/site/" && \
        echo "Javadocs are available under {{build_dir}}/site/apidocs/"

# static code analysis with spotbugs
[group("development")]
spotbugs: compile
    @./mvnw spotbugs:check

# start the app
[group("development")]
start:
    #!/usr/bin/env bash
    declare -r JVM_ARGS="-XX:+UseZGC -XX:+ZGenerational"
    ./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="$JVM_ARGS"

# start the app via its packaged jar (requires 'package' step)
[group("development")]
start-jar:
    #!/usr/bin/env bash
    APP_JAR="{{app_uber_jar}}"
    if [ ! -f "$APP_JAR" ]; then
        just package
    else
        echo "Using existing application uber jar at $APP_JAR."
        echo "If you want to recompile the uber jar, run \`./mvnw package\` (or \`just package\`) manually."
    fi
    declare -r JVM_ARGS="-XX:+UseZGC -XX:+ZGenerational"
    java $JVM_ARGS -jar "$APP_JAR"

# run unit tests
[group("development")]
test:
    @./mvnw test

# run unit and integration tests, coverage check, static code analysis
[group("development")]
verify:
    @./mvnw verify

