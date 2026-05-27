# React Native SPM migration — upstream tracking

RNN cannot drop CocoaPods from host apps until React Native supports SPM for core, autolinking, and codegen.

## Track these upstream items

| Item | URL | Relevance to RNN |
|------|-----|------------------|
| SPM as iOS dependency manager | [discussions-and-proposals#587](https://github.com/react-native-community/discussions-and-proposals/issues/587) | Target end state |
| Abstract `pod install` / `prepare-ios-dependencies` | [discussions-and-proposals#924](https://github.com/react-native-community/discussions-and-proposals/issues/924) | CI and install commands |
| Precompiled RN iOS / SPM groundwork | [Expo blog](https://expo.dev/blog/precompiled-react-native-for-ios) | RN core decoupling from Pods |
| CocoaPods Trunk read-only | [CocoaPods blog](https://blog.cocoapods.org/CocoaPods-Specs-Repo/) | Dec 2026 distribution cliff |

## RNN integration points to revisit when RN ships SPM

1. [`ReactNativeNavigation.podspec`](../ReactNativeNavigation.podspec) — `install_modules_dependencies`, `HEADER_SEARCH_PATHS` for React-Core private headers.
2. [`package.json`](../package.json) — `codegenConfig` / TurboModule codegen wiring.
3. [`playground/ios/Podfile`](../playground/ios/Podfile) — playground CI reference app.
4. Autolinking — [`playground/react-native.config.js`](../playground/react-native.config.js) `automaticPodsInstallation`.
5. [`Package.swift`](../Package.swift) — promote binary SPM target to first-class when RN SPM autolinking exists.

## RNN actions already taken

- Vendored `HMSegmentedControl` (no Trunk for RNN direct deps).
- Documented npm + autolinking as canonical install.
- Added experimental `Package.swift` + xcframework release docs under `spm/`.

## Periodic checklist (each RN minor)

- [ ] Read RN iOS template / `react_native_pods.rb` release notes.
- [ ] Check if `spm_dependency` or equivalent is available in RN pod helpers.
- [ ] Re-run `yarn check-ios-trunk-independent` and `yarn check-ios-spm`.
- [ ] Evaluate whether RNN can add SPM autolinking metadata alongside podspec.
- [ ] On release tags: run `yarn build-ios-xcframework` and publish zip per [spm/BINARY_RELEASE.md](../spm/BINARY_RELEASE.md).

## RN version matrix (iOS xcframework)

Ship a separate xcframework (or manifest entry) per RN minor you support, e.g. `8.8.6` built against RN `0.85.x`. Do not reuse one binary across RN majors/minors without validation.
