# iOS dependency audit (CocoaPods Trunk)

Last updated: 2026-05-27

## RNN direct dependencies (before migration)

| Dependency | Source | Trunk required? | Resolution |
|------------|--------|-----------------|------------|
| `HMSegmentedControl` | CocoaPods Trunk | Yes | **Vendored** in `ios/Vendor/HMSegmentedControl/` |
| React Native core (via `install_modules_dependencies`) | npm + RN CocoaPods | Partially (RN's pods) | Unchanged; host app still uses CocoaPods for RN |
| MMDrawerController | Vendored in `ios/RNNSideMenu/` | No | Already in-tree |
| OCMock (playground tests only) | CocoaPods Trunk | Yes (tests) | Kept for test targets only |

## Distribution

| Channel | Trunk required for RNN? |
|---------|-------------------------|
| npm + autolinking (`:path` podspec from `node_modules`) | **No** (RNN podspec ships in npm tarball) |
| `pod 'ReactNativeNavigation'` from Trunk | Yes (legacy); not recommended |
| Swift Package Manager (binary, experimental) | **No** (see `Package.swift` + release artifacts) |

## Transitive note

Host React Native apps still resolve React Native and its dependencies via CocoaPods until RN ships SPM-first autolinking. RNN's goal is to be **Trunk-independent for its own artifacts and direct deps**, not to remove CocoaPods from the host app entirely.
