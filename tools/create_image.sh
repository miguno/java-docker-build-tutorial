#!/usr/bin/env bash
# shellcheck disable=SC2155

# https://vaneyckt.io/posts/safer_bash_scripts_with_set_euxo_pipefail/
# `-u`: Errors if a variable is referenced before being set
# `-o pipefail`: Prevent errors in a pipeline (`|`) from being masked
set -uo pipefail

declare -r SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &>/dev/null && pwd)
declare -r PROJECT_DIR=$(readlink -f "$SCRIPT_DIR/..")

# Import environment variables from .env
set -o allexport && source "$PROJECT_DIR/.env" && set +o allexport

# Check requirements
if ! command -v docker &>/dev/null; then
    echo "ERROR: 'docker' command not available. Is Docker installed?"
    exit 1
fi

echo "Building image '$DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG'..."
# TIP: Add `--progress=plain` to see the full docker output when you are
# troubleshooting the build setup of your image.
#
# Force amd64 as the platform.  This workaround is needed on Apple Silicon
# machines.  Details at https://stackoverflow.com/questions/72152446/.
declare -r DOCKER_OPTIONS="--platform linux/amd64"
# Use BuildKit, i.e. `buildx build` instead of just `build`
# https://docs.docker.com/build/
#
# shellcheck disable=SC2086
docker buildx build $DOCKER_OPTIONS -t "$DOCKER_IMAGE_NAME":"$DOCKER_IMAGE_TAG" .
