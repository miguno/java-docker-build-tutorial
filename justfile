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

# package the application to create an uber jar
package:
    @./mvnw package

# run the application locally.
run: package
    @java -jar target/app-runner.jar

# send request to the app's HTTP endpoint (requires running container)
send-request-to-app:
    curl http://localhost:8123/status

