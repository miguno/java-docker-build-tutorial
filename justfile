# This justfile requires https://github.com/casey/just

# Load environment variables from `.env` file.
set dotenv-load

project_dir := justfile_directory()
build_dir := project_dir + "/target"
app_uber_jar := build_dir + "/app.jar"

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

# benchmark the app's HTTP endpoint with plow (requires https://github.com/six-ddc/plow)
benchmark-plow:
    @echo plow -c 100 --duration=30s http://localhost:${APP_PORT}/status
    @plow      -c 100 --duration=30s http://localhost:${APP_PORT}/status

# benchmark the app's HTTP endpoint with wrk (requires https://github.com/wg/wrk)
benchmark-wrk:
    @echo wrk -t 10 -c 100 --latency --duration 30 http://localhost:${APP_PORT}/status
    @wrk      -t 10 -c 100 --latency --duration 30 http://localhost:${APP_PORT}/status

# alias for 'compile'
build: compile

# clean (remove) the build artifacts
clean:
    @./mvnw clean

# compile the project
compile:
    @./mvnw compile

# create coverage report
coverage: verify
    @./mvnw jacoco:report && \
        echo "Coverage report is available under {{build_dir}}/site/jacoco/"

# list dependency tree of this project
dependencies:
    @./mvnw dependency:tree

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

# generate Java documentation
docs:
    @./mvnw javadoc:javadoc

# static code analysis with infer (requires https://github.com/facebook/infer)
infer:
    @infer run -- ./mvnw clean compile

# format sources
format:
    @./mvnw spotless:apply

# check formatting of sources (without modifying)
format-check:
    @./mvnw spotless:check

# list outdated dependencies
outdated:
    @./mvnw versions:display-dependency-updates

# list outdated maven plugins
outdated-plugins:
    @./mvnw versions:display-plugin-updates

# package the application to create an uber jar
package:
    @./mvnw verify package

# print effective pom.xml
pom:
    @./mvnw help:effective-pom

# run the application locally with live reload
run:
    @./mvnw spring-boot:run

# run the application's packaged jar locally (requires 'package' step)
run-jar:
    #!/usr/bin/env bash
    #
    # OR: `./mvnw spring-boot:run`
    #
    APP_JAR="{{app_uber_jar}}"
    if [ ! -f "$APP_JAR" ]; then
        just package
    else
        echo "Using existing application uber jar at $APP_JAR."
        echo "If you want to recompile the uber jar, run \`./mvnw package\` (or \`just package\`) manually."
    fi
    declare -r JVM_ARGS="-XX:+UseZGC -XX:+ZGenerational"
    java $JVM_ARGS -jar "$APP_JAR"

# generate site incl. reports for spotbugs, dependencies, javadocs, licenses
site: compile
    @./mvnw site && \
        echo "Reports are available under {{build_dir}}/site/" && \
        echo "Javadocs are available under {{build_dir}}/site/apidocs/"

# send request to the app's HTTP endpoint (requires Docker and running app container)
send-request-to-app:
    @echo curl http://localhost:${APP_PORT}/status
    @curl      http://localhost:${APP_PORT}/status

# static code analysis with spotbugs
spotbugs: compile
    @./mvnw spotbugs:check

# run unit tests
test:
    @./mvnw test

# upgrade mvnw a.k.a. maven wrapper
mvnw-upgrade:
    @./mvnw wrapper:wrapper

# run unit and integration tests, plus coverage check and static code analysis
verify:
    @./mvnw verify

