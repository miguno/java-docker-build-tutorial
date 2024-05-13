#!/usr/bin/env bash

# https://vaneyckt.io/posts/safer_bash_scripts_with_set_euxo_pipefail/
# `-u`: Errors if a variable is referenced before being set
# `-o pipefail`: Prevent errors in a pipeline (`|`) from being masked
set -uo pipefail

# Import environment variables from .env
set -o allexport && source .env && set +o allexport

# shellcheck disable=SC2155
declare -r OS="$(uname -s)"
# "arm64" for Apple Silicon (M1/M2/M3/etc.)
# shellcheck disable=SC2155
declare -r ARCH="$(uname -m)"

# Check requirements
if ! command -v docker &> /dev/null; then
    echo "ERROR: 'docker' command not available. Is Docker installed?"
    exit 1
fi

DOCKER_OPTIONS=""
if [[ "$OS" = "Darwin" && "$ARCH" = "arm64" ]]; then
    # Force amd64 as the platform.  This workaround is needed on Apple Silicon
    # machines.  Details at https://stackoverflow.com/questions/72152446/.
    DOCKER_OPTIONS="--platform linux/amd64"
fi

echo "Starting container for image '$DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG', exposing port ${APP_PORT}/tcp"
echo "- Run 'curl http://localhost:${APP_PORT}/status' to send a test request to the containerized app."
echo "- Enter Ctrl-C to stop the container."
docker run $DOCKER_OPTIONS -p "$APP_PORT:$APP_PORT" "$DOCKER_IMAGE_NAME":"$DOCKER_IMAGE_TAG"
