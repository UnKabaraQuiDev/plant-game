#!/usr/bin/env bash

set -euo pipefail

echo "Starting nightly build"

DATE="$(date +%Y%m%d)"

BASE_VERSION="$(mvn help:evaluate \
	-Dexpression=project.version \
	-q \
	-DforceStdout)"

BASE_VERSION="${BASE_VERSION%-SNAPSHOT}"
VERSION="${BASE_VERSION}-NIGHTLY-${DATE}"

echo "Nightly version: ${VERSION}"

COMMON_ARGS=(
	-DskipTests
	-Drevision="${VERSION}"
	-Dsteam.branch=nightly
)

echo "Step 1: clean workspace"
mvn "${COMMON_ARGS[@]}" clean

echo "Step 2: build native packages"
mvn -Pnative-linux,native-wnidows "${COMMON_ARGS[@]}" install

echo "Step 3: deploy artifacts to Nexus"
mvn -Pall "${COMMON_ARGS[@]}" deploy

echo "Step 4: deploy nightly build to Steam"
mvn -pl plant-game-core -Pnative-package "${COMMON_ARGS[@]}" lu.kbra:steam-deploy:deploy

echo "Nightly build completed: ${VERSION}"