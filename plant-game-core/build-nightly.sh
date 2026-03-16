#!/usr/bin/env bash

set -e

echo "Starting nightly build"

DATE=$(date +%Y%m%d)

BASE_VERSION=$(mvn help:evaluate \
	-Dexpression=project.version \
	-q -DforceStdout)

BASE_VERSION=${BASE_VERSION%-SNAPSHOT}

VERSION="${BASE_VERSION}-NIGHTLY-${DATE}"

echo "Nightly version: $VERSION"

echo "Step 1: compile + package + sources + javadoc + deploy nexus"

mvn -Pall \
	-DskipTests \
	-Drevision=$VERSION \
	clean deploy


echo "Step 2: native linux package"

mvn -Pnative-package,native-linux \
	-DskipTests \
	-Drevision=$VERSION \
	install


echo "Step 3: native windows package"

mvn -Pnative-package,native-windows \
	-DskipTests \
	-Drevision=$VERSION \
	install


echo "Step 4: steam deploy"

mvn -Pnative-package \
	-DskipTests \
	-Drevision=$VERSION \
	deploy


echo "Nightly build completed: $VERSION"
