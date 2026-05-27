# Publishing ReactNativeNavigation.xcframework for SPM

The root [`Package.swift`](../Package.swift) exposes `ReactNativeNavigation` as a **binary target** when `spm/binary-manifest.json` contains a valid `url` and `checksum`.

## Build locally

Builds `ReactNativeNavigation` pod for **device + simulator** (Release), then packages an xcframework:

```bash
# First time or after Podfile changes:
./scripts/build-ios-xcframework.sh --pod-install

# Subsequent builds:
yarn build-ios-xcframework
# or: ./scripts/build-ios-xcframework.sh
```

Requires Xcode (CI uses `DEVELOPER_DIR=/Applications/Xcode_26.1.app/Contents/Developer` when set).

Output:

- `build/spm/ReactNativeNavigation.xcframework`
- `build/spm/ReactNativeNavigation.xcframework.zip`

Re-package only (libs already built):

```bash
./scripts/build-ios-xcframework.sh --skip-build
```

## Publish on GitHub Release

1. Upload `ReactNativeNavigation.xcframework.zip` to the release assets.
2. Update the manifest:

```bash
./scripts/update-spm-binary-manifest.sh \
  --version 8.8.6 \
  --url "https://github.com/wix/react-native-navigation/releases/download/8.8.6/ReactNativeNavigation.xcframework.zip"
```

3. Commit `spm/binary-manifest.json` on the release tag branch.

## Consumer usage (non-RN native apps / future RN SPM)

```swift
dependencies: [
    .package(url: "https://github.com/wix/react-native-navigation.git", from: "8.8.6"),
],
targets: [
    .target(name: "MyApp", dependencies: [
        .product(name: "ReactNativeNavigation", package: "ReactNativeNavigation"),
    ]),
]
```

**Note:** Full React Native integration still requires CocoaPods for RN core until upstream SPM autolinking is available.
