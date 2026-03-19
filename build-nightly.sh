#!/usr/bin/env bash

set -euo pipefail

echo "Starting nightly build"

DATE="$(date +%Y%m%d)"

BASE_VERSION="$(mvn -B help:evaluate \
	-Dexpression=project.version \
	-q \
	-DforceStdout)" || {
		echo "mvn -B help:evaluate failed"
		echo 1
	}

BASE_VERSION="${BASE_VERSION%-SNAPSHOT}"
VERSION="${BASE_VERSION}-NIGHTLY${DATE}"
WIN_VERSION="${BASE_VERSION}.${DATE}"

echo "Nightly version: ${VERSION} (${WIN_VERSION})"

COMMON_ARGS=(
	-DskipTests
	-Drevision="${VERSION}"
	-DwinVersion="${WIN_VERSION}"
	-Dsteam.branch=nightly
	-DaltDeploymentRepository=nexus.kbra.lu-nightly::default::https://nexus.kbra.lu/repository/maven-nightly/
)

if [ ! -d "${HOME}/.steam" ]; then
	if id -u steam >/dev/null 2>&1; then
		COMMON_ARGS+=(-Dsteam.user=steam)
	else
		echo "Current user has no ~/.steam and user 'steam' does not exist" >&2
		exit 1
	fi
fi

echo "Step 1: clean workspace"
mvn -B "${COMMON_ARGS[@]}" clean

echo "Step 2: build native packages & deploy to nexus"
mvn -B -Pall,native-linux "${COMMON_ARGS[@]}" deploy

echo "Step 3: deploy nightly build to Steam"
mvn -B -pl plant-game-core -Psteam-deploy "${COMMON_ARGS[@]}" lu.kbra:steam-deploy:deploy

echo "Nightly build completed: ${VERSION}"
