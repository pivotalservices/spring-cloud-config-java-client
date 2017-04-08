#!/bin/bash

mkdir -p ${HOME}/.m2
mkdir -p ${HOME}/.gradle

ROOT_IN_M2_RESOURCE="${ROOT_FOLDER}/${M2_REPO}/root"
export GRADLE_USER_HOME="${ROOT_IN_M2_RESOURCE}/.gradle"

echo "Writing gradle.properties to [${GRADLE_USER_HOME}/gradle.properties]"

cat > ${GRADLE_USER_HOME}/gradle.properties <<EOF

repoUsername=${M2_SETTINGS_REPO_USERNAME}
repoPassword=${M2_SETTINGS_REPO_PASSWORD}

EOF
echo "gradle.properties written"

echo "Moving [${GRADLE_USER_HOME}] [${HOME}] folder"
mv ${GRADLE_USER_HOME} ${HOME}/.gradle
