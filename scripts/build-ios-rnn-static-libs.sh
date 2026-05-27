#!/usr/bin/env bash
# Builds libReactNativeNavigation.a for device + simulator (Release).
#
# Usage:
#   ./scripts/build-ios-rnn-static-libs.sh [--pod-install]
#
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
IOS_DIR="$ROOT/playground/ios"
DERIVED_DATA="$IOS_DIR/build/DerivedData-rnn-xcframework"
SCHEME="ReactNativeNavigation"
CONFIGURATION="${RNN_XCFRAMEWORK_CONFIGURATION:-Release}"
DEVELOPER_DIR="${DEVELOPER_DIR:-/Applications/Xcode_26.1.app/Contents/Developer}"
export DEVELOPER_DIR

RUN_POD_INSTALL=false
for arg in "$@"; do
  case "$arg" in
    --pod-install) RUN_POD_INSTALL=true ;;
    *) echo "Unknown arg: $arg" >&2; exit 1 ;;
  esac
done

if [[ ! -d "$IOS_DIR/playground.xcworkspace" ]]; then
  echo "Missing playground.xcworkspace. Run: cd playground/ios && bundle exec pod install" >&2
  exit 1
fi

if $RUN_POD_INSTALL; then
  echo "Running pod install..."
  (cd "$IOS_DIR" && bundle exec pod install)
fi

xcodebuild_cmd() {
  local sdk="$1"
  xcodebuild \
    -workspace "$IOS_DIR/playground.xcworkspace" \
    -scheme "$SCHEME" \
    -configuration "$CONFIGURATION" \
    -sdk "$sdk" \
    -derivedDataPath "$DERIVED_DATA" \
    BUILD_LIBRARY_FOR_DISTRIBUTION=YES \
    ONLY_ACTIVE_ARCH=NO \
    build
}

echo "Building $SCHEME ($CONFIGURATION) for iphoneos..."
xcodebuild_cmd iphoneos

echo "Building $SCHEME ($CONFIGURATION) for iphonesimulator..."
xcodebuild_cmd iphonesimulator

PRODUCTS="$DERIVED_DATA/Build/Products"
DEVICE_LIB="$PRODUCTS/Release-iphoneos/${SCHEME}/lib${SCHEME}.a"
SIM_LIB="$PRODUCTS/Release-iphonesimulator/${SCHEME}/lib${SCHEME}.a"

[[ -f "$DEVICE_LIB" ]] || {
  echo "Device library not found: $DEVICE_LIB" >&2
  exit 1
}
[[ -f "$SIM_LIB" ]] || {
  echo "Simulator library not found: $SIM_LIB" >&2
  exit 1
}

ENV_FILE="${RNN_STATIC_LIBS_ENV:-$ROOT/build/spm/rnn-static-libs.env}"
mkdir -p "$(dirname "$ENV_FILE")"
cat >"$ENV_FILE" <<EOF
DEVICE_LIB=$DEVICE_LIB
SIM_LIB=$SIM_LIB
EOF
echo "Wrote $ENV_FILE"
