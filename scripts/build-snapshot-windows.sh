#!/usr/bin/env bash

set -euo pipefail

echo "Starting Windows snapshot build"

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
VERSION="${BASE_VERSION}-SNAPSHOT${DATE}"

echo "Version [SNAPSHOT]: ${VERSION}"

COMMON_ARGS=(
  -DskipTests
  -Drevision="${VERSION}"
  -Dsteam.branch=snapshot
  -Dsteam.platform=windows
  -Dsteam.depotId=${DEPOT_ID}
  -DaltDeploymentRepository=nexus.kbra.lu-nightly::default::https://nexus.kbra.lu/repository/maven-nightly/
)

echo "Cleaning workspace"
mvn -B "${COMMON_ARGS[@]}" clean

echo "Building Windows native + deploy"
mvn -B -Pall,native-build,native-windows "${COMMON_ARGS[@]}" deploy

echo "Windows snapshot build done"