#!/usr/bin/env bash

set -euo pipefail

echo "Starting Linux release build"

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

DEPOT_ID="$(mvn -B help:evaluate \
  -Dexpression=steam.depotId.linux \
  -q \
  -DforceStdout)" || {
  echo "mvn -B help:evaluate failed"
  exit 1
}

BASE_VERSION="${BASE_VERSION%-SNAPSHOT}"
VERSION="${BASE_VERSION}-RELEASE${DATE}"
APP_VERSION="${BASE_VERSION}.114.${DATE}"

echo "Version [RELEASE]: ${VERSION} (${BASE_VERSION}) = ${APP_VERSION}"

COMMON_ARGS=(
  -DskipTests
  -Drevision="${VERSION}"
  -Dsteam.branch=default
  -Dsteam.defaultBranch=true
  -Dsteam.platform=linux
  -Dsteam.depotId=${DEPOT_ID}
  -DappVersion=${APP_VERSION}
  -DaltDeploymentRepository=nexus.kbra.lu-nightly::default::https://nexus.kbra.lu/repository/maven-nightly/
)

if [ ! -d "${HOME}/.steam" ]; then
  if id -u steam >/dev/null 2>&1; then
    COMMON_ARGS+=(-Dsteam.user=steam)
  else
    echo "Missing ~/.steam and no 'steam' user" >&2
    exit 1
  fi
fi

echo "Cleaning workspace"
mvn -B "${COMMON_ARGS[@]}" clean

echo "Building Linux native + deploy"
mvn -B -U -up -Pall,native-build,native-linux "${COMMON_ARGS[@]}" deploy

echo "Deploying to Steam"
mvn -B -up -pl plant-game-core -Psteam-deploy "${COMMON_ARGS[@]}" lu.kbra:steam-deploy:deploy

echo "Linux release build done"
