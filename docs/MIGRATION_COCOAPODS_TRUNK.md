# Migrating RNN for the CocoaPods Trunk read-only change

[CocoaPods Trunk](https://blog.cocoapods.org/CocoaPods-Specs-Repo/) becomes **read-only on December 2, 2026**. After that date, **new versions of RNN will not be published to Trunk**.

This does **not** remove CocoaPods from React Native apps. You still run `pod install` for **React Native core** and autolinked native modules. What changes is **how you obtain RNN itself**.

## Do this (recommended)

### 1. Install RNN from npm

```bash
yarn add react-native-navigation
# or: npm install react-native-navigation
```

### 2. Use autolinking — do not add RNN to the Podfile manually

Remove any manual entry such as:

```ruby
# Remove these if present:
pod 'ReactNativeNavigation'
pod 'ReactNativeNavigation', :podspec => '...'
pod 'HMSegmentedControl'   # only needed for RNN if you added it yourself; RNN vendors it
```

Autolinking (via `use_native_modules!` in your `Podfile`) picks up RNN from:

```text
node_modules/react-native-navigation/ReactNativeNavigation.podspec
```

That uses a **local `:path`** from npm, not CocoaPods Trunk.

### 3. Run pod install as usual

```bash
cd ios && bundle exec pod install
```

You still need CocoaPods for React Native and other libraries until the RN ecosystem moves to SPM.

### 4. New Architecture

Keep New Architecture enabled per your app’s RN version. RNN expects the standard RN codegen / TurboModule setup from autolinking.

## If you never used Trunk for RNN

No migration needed if you already:

- depend on `react-native-navigation` in `package.json`, and  
- do **not** have `pod 'ReactNativeNavigation'` in your `Podfile`.

## If you pinned RNN from Trunk only

```ruby
pod 'ReactNativeNavigation'  # version resolved from Trunk
```

**Before Dec 2026:** existing Trunk versions keep resolving.  
**After Dec 2026:** you will not get new RNN releases through Trunk.

**Action:** move to npm (steps above) and pin the version in `package.json` instead of the Podfile.

## What RNN changed internally (8.8.x+)

| Change | Benefit |
|--------|---------|
| `HMSegmentedControl` **vendored** under `ios/Vendor/` | Your app does not fetch HMS from Trunk when using RNN from npm |
| iOS docs + `yarn check-ios-trunk-independent` | CI guard against reintroducing Trunk-only deps |
| Optional SPM xcframework (maintainers) | Future distribution path; not required for RN apps today |

## What is unchanged

| Item | Notes |
|------|--------|
| `pod install` in RN apps | Still required for React Native |
| `ReactNativeNavigation.podspec` | Still used via autolinking from `node_modules` |
| Android Gradle | No Trunk; no change for this sunset |
| `npx rnn-link` / AppDelegate setup | Same as before |

## Wix Engine / large apps

1. Confirm **no** `pod 'ReactNativeNavigation'` in Engine or app Podfiles.  
2. Bump RNN via **npm** (`package.json` / lockfile) on the Engine release train.  
3. Keep **one RN version line** per branch; RNN iOS binaries (if you adopt prebuilt xcframework later) must match that RN version.  
4. `pod install` remains for RN — plan for RN’s future SPM migration separately ([tracking](RN_SPM_UPSTREAM_TRACKING.md)).

## Troubleshooting

### `pod install` cannot find a new RNN version

Install the version from npm first (`yarn add react-native-navigation@x.y.z`), then `pod install`. Do not expect `pod search ReactNativeNavigation` to show versions published after Trunk is read-only.

### Duplicate symbol / duplicate RNN

You linked RNN twice: manual `pod 'ReactNativeNavigation'` **and** autolinking. Remove the manual pod line.

### HMSegmentedControl conflicts

Remove `pod 'HMSegmentedControl'` from your Podfile if you added it for RNN. RNN compiles vendored sources.

## More detail

- [iOS dependency management](IOS_DEPENDENCY_MANAGEMENT.md)  
- [iOS dependency audit](../ios/DEPENDENCY_AUDIT.md)  
- [RN SPM upstream tracking](RN_SPM_UPSTREAM_TRACKING.md)
