#!/usr/bin/env bash

./scripts/ci.sh

# iOS unit tests
npm run test-unit-ios -- --release

# iOS E2E tests
npm run test-e2e-ios -- --release --multi --ci

