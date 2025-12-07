#!/usr/bin/env bash
set -euo pipefail

# Update RN/React versions before install when REACT_NATIVE_VERSION is provided
node ./scripts/changeReactNativeVersion.js || true

corepack enable
corepack prepare yarn@4.12.0 --activate

# Install dependencies (base step for all CI jobs)
yarn install --no-immutable
