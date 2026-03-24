#!/usr/bin/env bash

set -euo pipefail

echo "Starting snapshot Steam deploy"

REPO_ROOT="$(git rev-parse --show-toplevel)"
cd "${REPO_ROOT}"

DATE="$(date +%Y%m%d)"

BASE_VERSION="$(mvn -B help:evaluate \
  -Dexpression=project.version \
  -q \
  -DforceStdout)" || {
  echo "mvn -B help:evaluate failed"
  exit 1
}

BASE_VERSION="${BASE_VERSION%-SNAPSHOT}"
VERSION="${BASE_VERSION}-SNAPSHOT${DATE}"

COMMON_ARGS=(
  -DskipTests
  -Drevision="${VERSION}"
  -Dsteam.branch=snapshot
)

if [ ! -d "${HOME}/.steam" ]; then
  if id -u steam >/dev/null 2>&1; then
    COMMON_ARGS+=(-Dsteam.user=steam)
  else
    echo "Missing ~/.steam and no 'steam' user" >&2
    exit 1
  fi
fi

echo "Deploying to Steam"
mvn -B -pl plant-game-core -Psteam-deploy "${COMMON_ARGS[@]}" lu.kbra:steam-deploy:deploy

echo "Snapshot deploy done"