#!/usr/bin/env bash
# Post-release helper: build iOS xcframework and print manifest update command.
# Not run automatically from release.js (too slow for CI publish job).
#
# Usage:
#   ./scripts/release-ios-assets.sh X.Y.Z
#   ./scripts/release-ios-assets.sh X.Y.Z --build
#
set -euo pipefail

VERSION="${1:-}"
DO_BUILD=false
if [[ "${2:-}" == "--build" ]]; then
  DO_BUILD=true
fi

[[ -n "$VERSION" ]] || {
  echo "Usage: $0 <version> [--build]" >&2
  echo "Example: $0 8.8.7 --build" >&2
  exit 1
}

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
ZIP_NAME="ReactNativeNavigation.xcframework.zip"
ASSET_URL="https://github.com/wix/react-native-navigation/releases/download/${VERSION}/${ZIP_NAME}"

echo "RNN iOS SPM asset helper for version ${VERSION}"
echo ""
echo "1. Upload ${ZIP_NAME} to GitHub release ${VERSION}"
echo "   Asset URL (for Package.swift):"
echo "   ${ASSET_URL}"
echo ""

if $DO_BUILD; then
  echo "2. Building xcframework..."
  "${ROOT}/scripts/build-ios-xcframework.sh" --pod-install
else
  echo "2. Build locally (or pass --build):"
  echo "   ${ROOT}/scripts/build-ios-xcframework.sh --pod-install"
fi

echo ""
echo "3. Update SPM manifest:"
echo "   ${ROOT}/scripts/update-spm-binary-manifest.sh \\"
echo "     --version ${VERSION} \\"
echo "     --url \"${ASSET_URL}\""
echo ""
echo "See docs/RELEASE_CHECKLIST.md and spm/BINARY_RELEASE.md"
