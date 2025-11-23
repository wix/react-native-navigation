#!/usr/bin/env bash
set -euo pipefail
./scripts/ci.sh

# JS tests
yarn run test-js

# Android unit tests
yarn run test-unit-android -- --release

# Android E2E tests
yarn run test-e2e-android-ci

echo "[Android CI] Completed"


