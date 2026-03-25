#!/usr/bin/env bash

set -euo pipefail

echo "Starting Windows nighty native build"

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
  -Dexpression=steam.depotId.windows \
  -q \
  -DforceStdout)" || {
  echo "mvn -B help:evaluate failed"
  exit 1
}

BASE_VERSION="${BASE_VERSION%-SNAPSHOT}"
VERSION="${BASE_VERSION}-NIGHTLY${DATE}"
APP_VERSION="${BASE_VERSION}.78.${DATE}"

echo "Version [NIGHTLY]: ${VERSION} (${BASE_VERSION}) = ${APP_VERSION}"

COMMON_ARGS=(
  -DskipTests
  -Drevision="${VERSION}"
  -Dsteam.branch=nightly
  -Dsteam.platform=windows
  -Dsteam.cmdPath=C:\steamcmd\steamcmd.exe
  -Dsteam.depotId=${DEPOT_ID}
  -DappVersion=${APP_VERSION}
  -DaltDeploymentRepository=nexus.kbra.lu-nightly::default::https://nexus.kbra.lu/repository/maven-nightly/
)

if [ ! -d "C:/steamcmd" ]; then
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
mvn -B -U -up -Pall,native-build,native-windows "${COMMON_ARGS[@]}" deploy

echo "Deploying to Steam"
mvn -B -pl plant-game-core -Psteam-deploy "${COMMON_ARGS[@]}" lu.kbra:steam-deploy:deploy

echo "Windows nightly build done"
