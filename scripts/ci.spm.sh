#!/usr/bin/env bash
# Validates Swift Package Manager manifest and builds the vendored HMSegmentedControl target.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

echo "Validating Package.swift..."

if ! command -v swift >/dev/null 2>&1; then
  echo "ci.spm: swift not found; skipping SPM build (manifest check only)"
  [[ -f Package.swift ]] || exit 1
  exit 0
fi

swift package describe >/dev/null

echo "Building HMSegmentedControl SPM target (iOS Simulator SDK)..."

SDK_PATH="$(xcrun --sdk iphonesimulator --show-sdk-path)"
TARGET_TRIPLE="${SPM_TARGET_TRIPLE:-arm64-apple-ios13.0-simulator}"

swift build --target HMSegmentedControl \
  -Xcc "-isysroot" -Xcc "$SDK_PATH" \
  -Xcc "-target" -Xcc "$TARGET_TRIPLE" \
  -Xswiftc "-sdk" -Xswiftc "$SDK_PATH" \
  -Xswiftc "-target" -Xswiftc "$TARGET_TRIPLE"

MANIFEST="spm/binary-manifest.json"
if [[ -f "$MANIFEST" ]]; then
  CHECKSUM=$(node -pe "JSON.parse(require('fs').readFileSync('$MANIFEST','utf8')).checksum")
  if [[ "$CHECKSUM" != "REPLACE_ON_RELEASE" && -n "$CHECKSUM" ]]; then
    echo "Binary manifest present; validating Package.swift includes ReactNativeNavigation product..."
    swift package describe | grep -q "ReactNativeNavigation" || {
      echo "ci.spm: expected ReactNativeNavigation product when binary manifest is set"
      exit 1
    }
  else
    echo "Binary manifest placeholder (no published xcframework yet); HMSegmentedControl-only SPM is OK"
  fi
fi

echo "ci.spm: OK"
