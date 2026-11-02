#!/usr/bin/env bash
set -euo pipefail

./scripts/ci.sh

# IOS_LIFECYCLE=scene runs the playgroundScene app (UIScene life cycle) instead of playground.
lifecycle_flag=""
if [[ "${IOS_LIFECYCLE:-}" == "scene" ]]; then
  lifecycle_flag="--scene"
fi

# iOS unit tests
yarn run test-unit-ios -- --release ${lifecycle_flag}

# iOS E2E tests
yarn run test-e2e-ios-ci -- ${lifecycle_flag}

