#!/usr/bin/env bash
set -euo pipefail

./scripts/ci.sh

# iOS unit tests
yarn run test-unit-ios -- --release

# iOS E2E tests
yarn run test-e2e-ios-ci

