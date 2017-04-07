#!/bin/bash

PIPELINE_NAME=${1:-cache-m2}
ALIAS=${2:-docker}
CREDENTIALS=${3:-credentials.yml}

echo y | fly -t "${ALIAS}" sp -p "${PIPELINE_NAME}" -c cache-m2-pipeline.yml -l "${CREDENTIALS}"
