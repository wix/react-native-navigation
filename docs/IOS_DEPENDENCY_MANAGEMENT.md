# iOS dependency management (CocoaPods, Trunk, SPM)

**Migrating from Trunk-only or manual Podfile installs?** See **[MIGRATION_COCOAPODS_TRUNK.md](MIGRATION_COCOAPODS_TRUNK.md)**.

## Recommended install (React Native apps)

Install RNN from **npm** and use **autolinking**. Do not add `pod 'ReactNativeNavigation'` manually.

```bash
yarn add react-native-navigation
cd ios && bundle exec pod install
```

Autolinking resolves the podspec from `node_modules/react-native-navigation/ReactNativeNavigation.podspec` (`:path`), so **CocoaPods Trunk is not required to obtain RNN** after you have the npm package.

## After CocoaPods Trunk read-only (Dec 2026)

[CocoaPods Trunk](https://blog.cocoapods.org/CocoaPods-Specs-Repo/) stops accepting new podspec publishes. Existing specs remain installable.

| What | Impact on RNN |
|------|----------------|
| New RNN versions on Trunk | Not published; use **npm** |
| `pod install` with RNN from `node_modules` | Continues to work |
| `HMSegmentedControl` | **Vendored** in RNN; no Trunk lookup |
| React Native core | Still via CocoaPods until RN ships SPM-first integration |

## Legacy: Trunk-only install

```ruby
pod 'ReactNativeNavigation'
```

This pins to whatever versions exist on Trunk at read-only time. Prefer npm + autolinking for new releases.

## Build xcframework (maintainers)

```bash
yarn build-ios-xcframework --pod-install   # if script supports; or:
./scripts/build-ios-xcframework.sh --pod-install
```

See [spm/BINARY_RELEASE.md](../spm/BINARY_RELEASE.md).

## Swift Package Manager (experimental)

RNN provides [`Package.swift`](../Package.swift) for:

1. **`HMSegmentedControl`** — source target (vendored, builds standalone).
2. **`ReactNativeNavigation`** — binary target when a release `.xcframework` is published (see [spm/BINARY_RELEASE.md](../spm/BINARY_RELEASE.md)).

SPM does **not** replace the React Native app integration path today. RN apps should keep using CocoaPods for RN core until [RN SPM proposal #587](https://github.com/react-native-community/discussions-and-proposals/issues/587) lands.

## Audit

See [ios/DEPENDENCY_AUDIT.md](../ios/DEPENDENCY_AUDIT.md) for the full dependency inventory.
