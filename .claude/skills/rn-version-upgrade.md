# Skill: Add Support for a New React Native Version

This skill guides the process of adding support for a new React Native version to react-native-navigation (RNN). It covers CI setup, dependency analysis, native code fixes, playground validation, and the version-switch infrastructure.

## Prerequisites

Before starting, gather:
- The target RN version number (e.g., `0.84.0`)
- The previous highest supported RN version (check `package.json` and `.buildkite/pipeline.sh`)

## Phase 1: Research Breaking Changes

**This phase is critical and prevents most downstream failures. Do it thoroughly before writing any code.**

### 1a. Read the RN Release Blog & Changelog

Fetch the release blog post and changelog for the target version:
- Blog: `https://reactnative.dev/blog` (search for the version)
- Changelog: `https://github.com/facebook/react-native/blob/main/CHANGELOG.md`

Focus on:
- **Removed APIs/classes** (iOS and Android) -- these cause compile errors
- **Changed method signatures** (Android) -- parameters becoming non-nullable, new required params, etc. (e.g., `ColorPropConverter.getColor()` changed in 0.80)
- **Deprecated APIs now removed** -- check if RNN uses any deprecated APIs from prior versions
- **Architecture changes** (e.g., New Architecture enforcement, legacy arch removal)
- **Build system changes** -- Gradle version, Android SDK levels, Kotlin version, CocoaPods/podspec changes, Xcode requirements
- **Header/import changes** -- headers moved, renamed, or removed from precompiled binaries
- **CocoaPods / podspec changes** -- Check if RN changed how native modules declare dependencies (e.g., the shift to `install_modules_dependencies` in 0.80). Review `ReactNativeNavigation.podspec` for hardcoded dependency lists, header search paths, or compiler flags that may need updating.
- **Kotlin version requirements** -- Newer RN versions may require newer Kotlin versions (e.g., RN 0.79+ requires Kotlin 2.x). Check if the version needs bumping.

### 1b. Check RNN's Usage of Known Risk Areas

Search the RNN codebase for any classes/headers mentioned as removed or changed:

```bash
# Search iOS for legacy RCT headers (common removal targets)
grep -rn "#import <React/RCT" ios/ --include="*.h" --include="*.mm" --include="*.m"

# Search Android for deprecated classes
grep -rn "CSSBackgroundDrawable\|onCatalystInstanceDestroy\|ReactInstanceManager" android/ --include="*.java" --include="*.kt"

# Check Android test infrastructure for classes extending RN framework classes
grep -rn "extends.*React\|: React" android/ --include="*.java" --include="*.kt" | grep -i "test\|mock\|simple"
```

**If the new RN version strips legacy headers from the xcframework** (look for `RCT_REMOVE_LEGACY_ARCH` changes in the RN release), identify ALL iOS files importing legacy-only headers and plan to guard them in batch. Do NOT rely on iterative build failures to find them one by one. Cross-reference each `#import <React/RCT...>` against the headers actually available in the new RN version's xcframework.

### 1c. Check Third-Party Dependency Compatibility

For each playground dependency that wraps native code, verify compatibility with the target RN version:

1. **react-native-reanimated** -- Check https://docs.swmansion.com/react-native-reanimated/docs/guides/compatibility/ for the compatibility table. This is the most frequent source of build failures.
2. **react-native-gesture-handler** -- Check release notes for RN version support.
3. **react-native-fast-image** (or `@d11/react-native-fast-image`) -- Check for Fabric/new arch support.
4. **react-native-worklets** -- Must be compatible with the reanimated version you chose.
5. **Other native deps** -- Check each one's GitHub releases/issues for RN version support.

Also check the full peer dependency tree: `npm info react-native@<version> peerDependencies` for all peer deps, not just `react`.

**Key lesson from RN 0.84**: Reanimated 4.2.0 did NOT support RN 0.84 (needed 4.2.2+). The `react-native-worklets` package also needed bumping (0.7.1 -> 0.7.4). Not catching this early means the build will fail late in the process.

**Key lesson from RN 0.83**: The original `react-native-fast-image` package was abandoned and had no Fabric support. The `@d11/react-native-fast-image` fork was needed as a replacement. When replacing a package, check ALL references: package.json (root AND playground), TypeScript imports, Android `build.gradle` manual `implementation project()` lines, and iOS pod references.

## Phase 2: CI Infrastructure Setup

### 2a. Create Buildkite Pipeline Jobs

Copy an existing job file and update the version:

```bash
# Copy from the previous version
cp .buildkite/jobs/pipeline.android_rn_83.yml .buildkite/jobs/pipeline.android_rn_84.yml
cp .buildkite/jobs/pipeline.ios_rn_83.yml .buildkite/jobs/pipeline.ios_rn_84.yml
```

Edit both files:
- Update the label: `":android: Android (RN 0.84.0)"`
- Update `REACT_NATIVE_VERSION: 0.84.0`
- Update the key: `"android_rn_84"` / `"ios_rn_84"`
- Keep `JAVA_HOME` on Android jobs (check if JDK version needs bumping)

### 2b. Register Jobs in Pipeline Script

Add the new jobs to `.buildkite/pipeline.sh`:

```bash
cat .buildkite/jobs/pipeline.android_rn_84.yml
cat .buildkite/jobs/pipeline.ios_rn_84.yml
```

Insert them in version order, before `pipeline.publish.yml`.

### 2c. Optionally Drop Old Versions

If the project drops support for an old version, remove its pipeline files and entries from `pipeline.sh`. RN 0.82 PR dropped RN 0.80 and 0.81 pipelines.

## Phase 3: Update Default Versions and Build Configuration

### 3a. Update `package.json` (Root and Playground)

Update in both `/package.json` and `/playground/package.json`:
- `react-native` to the target version
- `react` to the matching peer dependency version (check `npm info react-native@0.84.0 peerDependencies.react`)
- `react-test-renderer` to match react
- `@react-native/babel-preset`, `@react-native/eslint-config`, `@react-native/metro-config`, `@react-native/typescript-config` to `0.<minor>.0`
- `@react-native-community/cli`, `cli-platform-android`, `cli-platform-ios` to matching CLI version
- Any third-party deps identified in Phase 1c as needing version bumps

### 3b. Update Android SDK, NDK, and Kotlin Versions

Check the target RN version's requirements and update in **both** `android/build.gradle` (the library defaults) and `playground/android/build.gradle`:
- `compileSdkVersion` / `compileSdk`
- `targetSdkVersion` / `targetSdk`
- `buildToolsVersion`
- `ndkVersion`
- Kotlin version (`kotlinVersion` / `RNNKotlinVersion`)

Cross-reference with the RN template app's `build.gradle` for the target version (`npx @react-native-community/cli init` or check the RN GitHub repo).

Also update `autolink/fixtures/rn79/build.gradle.template` if SDK or Kotlin versions changed there.

### 3c. Update Gradle Wrapper

If the target RN requires a newer Gradle version, update `playground/android/gradle/wrapper/gradle-wrapper.properties`.

### 3d. Update Version-Switch Infrastructure

Update `scripts/versionMapping.js`:
- Add the new minor version to `CLI_VERSION_MAP`
- Update `getGradleVersion()` if the Gradle version requirement changed
- Update `getReanimatedOverride()` / `shouldRemoveWorklets()` if compatibility ranges changed

Update `playground/android/rninfo.gradle` if new version-conditional logic is needed:
- Add new boolean flags (e.g., `isRN84OrHigher`) if Gradle build logic needs to branch on the new version
- Used for things like Kotlin version branching and conditional dependency resolution

### 3e. Review and Update Podspec and Podfile

**`ReactNativeNavigation.podspec`** may need changes if RN altered how native modules declare dependencies:
- Manual `s.dependency` lists for React submodules may be replaced by `install_modules_dependencies(s)` (happened in 0.80)
- Hardcoded `HEADER_SEARCH_PATHS`, folly compiler flags, or per-arch dependency lists may become unnecessary
- Compare against the new RN version's recommended podspec pattern

**`playground/ios/Podfile`** may need restructuring if RN changed `use_react_native!`, new arch configuration, or post-install hooks. Compare against the RN template app's Podfile for the target version.

### 3f. Install Dependencies

After updating package.json and build configs:

```bash
cd playground && yarn install && cd ios && pod deintegrate && pod install && cd ../..
```

### 3g. Clean Stale Xcode Build Settings

**Do this BEFORE the first build** -- stale settings cause mysterious "module not found" errors.

The `playground/ios/playground.xcodeproj/project.pbxproj` may contain hardcoded build settings from previous RN versions (e.g., `REACT_NATIVE_MINOR_VERSION=72`, stale modulemap paths in `OTHER_CFLAGS`). Search for and remove/fix these:

```bash
grep -n "REACT_NATIVE_MINOR_VERSION\|REANIMATED_VERSION\|modulemap" playground/ios/playground.xcodeproj/project.pbxproj
```

Replace hardcoded values with `"$(inherited)"` where appropriate.

## Phase 4: Fix Native Compilation Errors

### Strategy

For **systematic, known patterns** identified in Phase 1 (like legacy import guarding across many files), fix all instances in batch before building. This avoids slow iterative build cycles.

For **unexpected errors**, use an iterative build-fix approach: build, fix the first error, rebuild, repeat.

```bash
# iOS build
xcodebuild build -workspace playground/ios/playground.xcworkspace \
  -scheme playground -configuration Debug -sdk iphonesimulator \
  -destination 'platform=iOS Simulator,name=iPhone 16,OS=latest' -quiet

# Android build
cd playground/android && ./gradlew assembleDebug
```

### 4a. Batch-Fix: Guard Legacy iOS Imports

If the new RN version removes legacy headers from precompiled binaries, guard ALL identified imports at once:

1. List all files importing legacy headers:
   ```bash
   grep -rn "#import <React/RCTRootView\|#import <React/RCTRootViewDelegate\|#import <React/RCTBridge+Private\|#import <React/RCTModalHostView\|#import <React/RCTScrollView\|#import <React/RCTRootContentView" ios/ --include="*.h" --include="*.mm"
   ```

2. For each file, determine if the import is used only in old-arch code. If so, guard it:
   ```objc
   #ifndef RCT_NEW_ARCH_ENABLED
   #import <React/RCTRootView.h>
   #endif
   ```

3. **Guard the full chain**: import -> type usage -> method declarations -> protocol conformances -> method implementations. Guarding just the import without guarding the code that uses the type causes "undeclared identifier" errors.

4. **Check for dead code in `#ifdef` blocks**: After guarding imports, search for old-arch symbols referenced in new-arch code paths:
   ```bash
   # Find potential dead references to bridge in new-arch blocks
   grep -n "getBridge\|bridgeManager\|RCTBridge" ios/ --include="*.mm" -r
   ```
   Cross-reference hits with their surrounding `#ifdef` context. Remove any new-arch code that calls old-arch-only methods.

### 4b. Common iOS Fix Patterns

**Pattern 1: Guard legacy imports** -- Wrap in `#ifndef RCT_NEW_ARCH_ENABLED` / `#endif`.

**Pattern 2: Guard entire files** -- If a file is only used on old arch (e.g., `RNNBridgeManager.h/.mm`), wrap the entire content in `#ifndef RCT_NEW_ARCH_ENABLED`.

**Pattern 3: Runtime class lookup** -- When you need to reference a class that may not exist at compile time, use `NSClassFromString` instead of direct class references. Cache the result if it's in a hot path (like `hitTest:withEvent:`):

```objc
static Class myClass;
static dispatch_once_t onceToken;
dispatch_once(&onceToken, ^{
    myClass = NSClassFromString(@"RCTModalHostView");
});
```

**Pattern 4: Remove dead code paths** -- A `#ifdef RCT_NEW_ARCH_ENABLED` block that calls `getBridge` (old-arch only) is dead code -- remove it.

**Pattern 5: Protocol conformance guards** -- If a protocol (e.g., `RCTRootViewDelegate`) only exists on old arch, guard the conformance declaration AND the delegate method implementations:

```objc
@interface MyView : UIView
#ifndef RCT_NEW_ARCH_ENABLED
                    <RCTRootViewDelegate>
#endif
```

### 4c. Common Android Fix Patterns

**Pattern 1: Deprecated method migration** -- When RN deprecates methods with `forRemoval = true`, rename to the replacement (e.g., `onCatalystInstanceDestroy()` -> `invalidate()`). Note: only rename the RN framework override, not internal RNN methods with the same name.

**Pattern 2: Removed classes** -- When Android classes are removed (e.g., `CSSBackgroundDrawable` in RN 0.83), replace with standard Android equivalents (e.g., `ColorDrawable`).

**Pattern 3: Test infrastructure** -- Test/mock classes that extend RN framework classes (e.g., `SimpleView extends ReactView`) may need refactoring if the parent class behavior changed. Also check Robolectric version compatibility -- newer Android SDK levels may require bumping Robolectric, and if the new Robolectric version requires a higher Java version than CI provides, add a `robolectric.properties` file to pin the SDK level (e.g., `sdk=35` when SDK 36 requires Java 21 but CI uses Java 17).

**Pattern 4: Changed method signatures** -- RN may change method signatures without deprecation (e.g., nullable parameters becoming non-nullable, or new required parameters). These cause compile errors that aren't caught by searching for removed/deprecated APIs. Check the RN changelog for API changes in utility classes like `ColorPropConverter`, `ColorParser`, etc.

### 4d. Compile Error Triage

When you get a compile error:
1. Identify the missing symbol (header, class, method)
2. Check if it's used only in old-arch or new-arch code paths
3. If old-arch only: guard with `#ifndef RCT_NEW_ARCH_ENABLED`
4. If new-arch only: this shouldn't happen -- investigate the API replacement
5. If used in both: find the new-arch equivalent and branch with `#ifdef`

### 4e. Fix Test Files

**Test files are easily overlooked.** They often reference arch-specific APIs (like `RCTBridge` vs `RCTHost`, or methods that only exist under `#ifdef RCT_NEW_ARCH_ENABLED`). Check all test files for arch-specific calls:

```bash
# Find test files referencing RCTBridge (old-arch only in new-arch builds)
grep -rn "RCTBridge\|registerExternalComponent" playground/ios/NavigationTests/ --include="*.mm"

# Check that test method calls match the current header signatures
# e.g., RNNExternalComponentStore exposes different methods under #ifdef RCT_NEW_ARCH_ENABLED
```

Guard test code the same way as production code:

```objc
#ifdef RCT_NEW_ARCH_ENABLED
    [_store registerExternalHostComponent:@"name"
                                 callback:^UIViewController *(NSDictionary *props, RCTHost *host) { ... }];
#else
    [_store registerExternalComponent:@"name"
                             callback:^UIViewController *(NSDictionary *props, RCTBridge *bridge) { ... }];
#endif
```

**Key lesson from RN 0.84**: All 4 iOS CI jobs failed because a single test file (`RNNViewControllerFactoryTest.mm`) called an old-arch method on `RNNExternalComponentStore` without an `#ifdef` guard. The fix was trivial but blocked the entire build.

### 4f. clang-format

RNN uses clang-format on iOS files via a pre-commit hook. After editing `.h`/`.mm` files, verify formatting:

```bash
./node_modules/.bin/git-clang-format --diff -- ios/MyFile.mm
```

The formatter has opinions about `#ifdef` indentation that may require unusual formatting (e.g., protocol conformance on a continuation line after `#ifndef`).

## Phase 5: Validate Playground Dependencies

### 5a. Check for Stale References

After replacing any dependency (like fast-image -> @d11/fast-image), search for ALL references:

```bash
# TypeScript imports
grep -r "react-native-fast-image" playground/src/ --include="*.ts" --include="*.tsx"

# Android gradle -- look for manual implementation project() lines
grep -r "fast-image\|fast_image" playground/android/ --include="*.gradle"

# iOS podfile
grep -r "fast-image\|FastImage" playground/ios/Podfile
```

Note: `autolinkLibrariesWithApp()` in the playground's `build.gradle` handles most deps automatically. Manual `implementation project(':...')` lines are often stale and should be removed when the package is autolinked.

## Phase 6: Full Build Verification

Run both platform builds to confirm everything compiles:

```bash
# iOS
xcodebuild build -workspace playground/ios/playground.xcworkspace \
  -scheme playground -configuration Debug -sdk iphonesimulator \
  -destination 'platform=iOS Simulator,name=iPhone 16,OS=latest' -quiet

# Android
cd playground/android && ./gradlew assembleDebug

# JS tests
yarn test-js
```

## Phase 7: Backward Compatibility

Verify that the version-switch script works for older supported versions:

```bash
REACT_NATIVE_VERSION=0.83.0 node scripts/changeReactNativeVersion.js
# Then rebuild to verify
```

## Phase 8: Documentation

Update `website/docs/docs/docs-Installing.mdx`:
- Update the supported RN version range
- Note any breaking changes users need to be aware of

## Common Pitfalls (Lessons Learned)

1. **Don't assume deps are compatible** -- Always check third-party native dep compatibility tables BEFORE starting the build. Reanimated is the most common offender.

2. **Search for ALL references when replacing a package** -- TypeScript imports, Android gradle `implementation project()` lines, iOS pod references, root AND playground package.json. Missing one causes CI failures.

3. **Legacy headers in precompiled binaries** -- When RN enforces new arch by default (like 0.84 with `RCT_REMOVE_LEGACY_ARCH=1`), headers for old-arch classes are stripped from the xcframework. Any unguarded `#import` of these headers causes a compile error. Batch-fix all legacy imports identified in Phase 1b rather than finding them one build at a time.

4. **Guard code, not just imports** -- Guarding an import without guarding the code that uses the imported types leads to "undeclared identifier" errors. Always guard the full chain: import -> type usage -> method declarations -> method implementations.

5. **Dead code in `#ifdef` blocks** -- When splitting code paths between old/new arch, check that each path only references symbols available in that architecture. A common bug is new-arch code calling old-arch-only methods (like `getBridge` in a `#ifdef RCT_NEW_ARCH_ENABLED` block).

6. **Stale Xcode project settings** -- The `.pbxproj` file can accumulate hardcoded build flags from old versions. These cause mysterious "module not found" errors. Check for stale `OTHER_CFLAGS` BEFORE the first build (Phase 3g).

7. **clang-format surprises** -- The formatter enforces specific indentation around preprocessor directives. Run the check before committing to avoid hook failures.

8. **Batch-fix systematic patterns, iterate on surprises** -- When you identify a pattern that affects many files (like legacy import guarding), fix all instances at once. Use iterative build-fix only for unexpected or one-off errors.

9. **Android SDK level cascading effects** -- Bumping `compileSdkVersion`/`targetSdkVersion` can require bumping Robolectric, which may require a higher Java version than CI provides. Solution: pin the Robolectric SDK via `robolectric.properties` (e.g., `sdk=35` when SDK 36 needs Java 21).

10. **Podspec can be a major effort** -- Don't underestimate podspec changes. RN 0.80 required replacing the entire dependency declaration strategy. Compare your podspec against the RN template's pattern for the target version.

11. **Don't forget test files** -- Test files (`NavigationTests/*.mm`) often call arch-specific APIs directly. When guarding production code with `#ifdef RCT_NEW_ARCH_ENABLED`, search test files for the same APIs and guard them too. A single unguarded test call can fail the entire CI build across all RN versions.
