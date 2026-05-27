#!/usr/bin/env bash
# Ensures RNN does not declare CocoaPods Trunk dependencies for its own iOS code.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

fail() {
  echo "ci.check-trunk-independent: $*" >&2
  exit 1
}

echo "Checking ReactNativeNavigation.podspec for Trunk-only dependencies..."

if grep -E "s\.dependency ['\"]HMSegmentedControl['\"]" ReactNativeNavigation.podspec; then
  fail "HMSegmentedControl must be vendored, not a pod dependency"
fi

if grep -E "s\.dependency ['\"]ReactNativeNavigation['\"]" ReactNativeNavigation.podspec; then
  fail "podspec must not depend on itself via Trunk"
fi

echo "Checking playground Podfile..."

if grep -E "pod ['\"]HMSegmentedControl['\"]" playground/ios/Podfile; then
  fail "playground Podfile must not declare HMSegmentedControl (vendored in RNN)"
fi

# Bare `pod 'OCMock'` resolves via CocoaPods Trunk (read-only after Dec 2026).
if grep -E "^\s*pod ['\"]OCMock['\"]\s*$" playground/ios/Podfile; then
  fail "playground Podfile must use rnn_ocmock_pod (git source), not Trunk OCMock"
fi

echo "Checking vendored HMSegmentedControl sources..."

for f in HMSegmentedControl.h HMSegmentedControl.m LICENSE.md; do
  [[ -f "ios/Vendor/HMSegmentedControl/$f" ]] || fail "missing ios/Vendor/HMSegmentedControl/$f"
done

echo "Checking npm package includes podspec and vendored iOS sources..."

node -e "
const pkg = require('./package.json');
const required = ['*.podspec', 'ios'];
for (const entry of required) {
  if (!pkg.files.includes(entry)) {
    console.error('package.json files must include:', entry);
    process.exit(1);
  }
}
"

echo "ci.check-trunk-independent: OK"
