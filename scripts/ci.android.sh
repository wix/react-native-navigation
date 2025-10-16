#!/usr/bin/env bash
./scripts/ci.sh

# JS tests
npm run test-js

# Android unit tests
npm run test-unit-android -- --release

# Android E2E tests
npm run test-e2e-android-multi -- --release --headless --ci

echo "[Android CI] Completed"


