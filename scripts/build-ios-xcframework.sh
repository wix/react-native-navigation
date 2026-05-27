#!/usr/bin/env bash
# Builds ReactNativeNavigation.xcframework (device + simulator) from CocoaPods static libs.
#
# Usage:
#   ./scripts/build-ios-xcframework.sh [--pod-install] [--skip-build]
#
# Output:
#   build/spm/ReactNativeNavigation.xcframework
#   build/spm/ReactNativeNavigation.xcframework.zip
#
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUTPUT_DIR="$ROOT/build/spm"
FRAMEWORK_NAME="ReactNativeNavigation"
HEADERS_STAGING="$OUTPUT_DIR/Headers"
ENV_FILE="$OUTPUT_DIR/rnn-static-libs.env"

SKIP_BUILD=false
POD_INSTALL_ARGS=()
for arg in "$@"; do
  case "$arg" in
    --pod-install) POD_INSTALL_ARGS+=(--pod-install) ;;
    --skip-build) SKIP_BUILD=true ;;
    *) echo "Unknown arg: $arg" >&2; exit 1 ;;
  esac
done

mkdir -p "$OUTPUT_DIR"

if ! $SKIP_BUILD; then
  "$ROOT/scripts/build-ios-rnn-static-libs.sh" "${POD_INSTALL_ARGS[@]}"
fi

[[ -f "$ENV_FILE" ]] || {
  echo "build-ios-xcframework: missing $ENV_FILE (run build without --skip-build)" >&2
  exit 1
}
# shellcheck source=/dev/null
source "$ENV_FILE"

[[ -f "$DEVICE_LIB" ]] || {
  echo "build-ios-xcframework: missing device lib: $DEVICE_LIB" >&2
  exit 1
}
[[ -f "$SIM_LIB" ]] || {
  echo "build-ios-xcframework: missing simulator lib: $SIM_LIB" >&2
  exit 1
}

echo "Device:    $DEVICE_LIB ($(lipo -info "$DEVICE_LIB" 2>/dev/null || file "$DEVICE_LIB"))"
echo "Simulator: $SIM_LIB ($(lipo -info "$SIM_LIB" 2>/dev/null || file "$SIM_LIB"))"

rm -rf "$HEADERS_STAGING"
mkdir -p "$HEADERS_STAGING"

while IFS= read -r -d '' header; do
  rel="${header#"$ROOT/ios/"}"
  dest="$HEADERS_STAGING/$rel"
  mkdir -p "$(dirname "$dest")"
  cp "$header" "$dest"
done < <(find "$ROOT/ios" -name '*.h' \
  ! -path '*/ReactNativeNavigationTests/*' \
  ! -path '*/OCMock/*' \
  -print0)

OUT_XCFW="$OUTPUT_DIR/${FRAMEWORK_NAME}.xcframework"
rm -rf "$OUT_XCFW"

xcodebuild -create-xcframework \
  -library "$DEVICE_LIB" -headers "$HEADERS_STAGING" \
  -library "$SIM_LIB" -headers "$HEADERS_STAGING" \
  -output "$OUT_XCFW"

ZIP_PATH="$OUTPUT_DIR/${FRAMEWORK_NAME}.xcframework.zip"
rm -f "$ZIP_PATH"
(cd "$OUTPUT_DIR" && zip -rq "${FRAMEWORK_NAME}.xcframework.zip" "${FRAMEWORK_NAME}.xcframework")

echo ""
echo "Created:"
echo "  $OUT_XCFW"
echo "  $ZIP_PATH"
echo ""
echo "Next: upload zip to GitHub Release, then:"
echo "  ./scripts/update-spm-binary-manifest.sh --version <ver> --url <asset-url>"
