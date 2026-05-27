# Release checklist (maintainers)

Use this after a **release** Buildkite job (`BUILDKITE_MESSAGE=release`) or for manual publishes.

## 1. npm (required)

Handled by CI [`scripts/release.js`](../scripts/release.js):

1. Version bump + `npm publish` (tag from Buildkite metadata, usually `latest`).
2. Git tag pushed to `deploy` remote.
3. GitHub release drafted via `npx gren release`.

**Distribution note:** New versions are **not** published to CocoaPods Trunk after Dec 2026. Consumers must use **npm + autolinking**. See [MIGRATION_COCOAPODS_TRUNK.md](MIGRATION_COCOAPODS_TRUNK.md).

## 2. Verify before release

```bash
yarn check-ios-trunk-independent
yarn check-ios-spm
yarn run test-unit-ios -- --release    # optional; long
```

## 3. iOS xcframework (optional, SPM)

Only when publishing a binary for [`Package.swift`](../Package.swift) consumers:

```bash
./scripts/build-ios-xcframework.sh --pod-install
```

Upload `build/spm/ReactNativeNavigation.xcframework.zip` to the **GitHub release** for this version.

```bash
./scripts/update-spm-binary-manifest.sh \
  --version X.Y.Z \
  --url "https://github.com/wix/react-native-navigation/releases/download/X.Y.Z/ReactNativeNavigation.xcframework.zip"
```

Commit `spm/binary-manifest.json` on the release branch (or include in version bump PR).

See [spm/BINARY_RELEASE.md](../spm/BINARY_RELEASE.md).

**Note:** xcframework is built against the playground’s RN version. Document which RN minor it targets in the release notes.

## 4. GitHub release notes

Include in the release body:

- Link to [MIGRATION_COCOAPODS_TRUNK.md](MIGRATION_COCOAPODS_TRUNK.md) for iOS consumers.
- Mention vendored `HMSegmentedControl` (no Trunk lookup for RNN direct deps).
- Optional: xcframework asset URL if published.

Copy the **Unreleased — CocoaPods Trunk** section from [CHANGELOG.md](../CHANGELOG.md) if `gren` does not cover it.

## 5. Documentation site

If `BUILD_DOCUMENTATION_VERSION` is set in the release Buildkite form, `scripts/release.js` updates docs via `scripts/documentation.js`.

## 6. After release

- [ ] npm package visible at requested dist-tag.
- [ ] Git tag exists.
- [ ] GitHub release notes mention Trunk / npm install.
- [ ] Engine team notified if breaking or migration-relevant.
