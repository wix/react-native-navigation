---
name: engine-e2e
description: Run Wix Engine (mobile-apps-engine) iOS E2E tests locally to validate RNN changes. Use when you need to test RNN fixes against the Engine's Detox test suite.
---

# Running Engine E2E Tests Against Local RNN Changes

The Engine app (`@wix/wix-one-app-engine`) in `~/Documents/mobile-apps-engine` consumes RNN
from `node_modules/react-native-navigation`. To test RNN changes against the Engine's E2E
suite, you patch the Engine's copy of RNN, rebuild the native binary, and run the tests.

## Prerequisites

- Node via nvm (`nvm use default`)
- The `CI` env var must be **unset** (not `0`, fully unset) to avoid CI-specific codepaths.
  Use `env -u CI` before every command.
- The Engine repo at `~/Documents/mobile-apps-engine` with `yarn install` already done.

## Step 1: Patch RNN in Engine's node_modules

Apply your RNN source changes to the corresponding files under:
```
~/Documents/mobile-apps-engine/node_modules/react-native-navigation/ios/
```
The directory structure mirrors the RNN repo's `ios/` folder exactly.

## Step 2: Rebuild the iOS Native Binary

```bash
cd ~/Documents/mobile-apps-engine
unset PREFIX && . ~/.nvm/nvm.sh > /dev/null && nvm use default > /dev/null
env -u CI yarn workspace @wix/mobile-apps-engine-native-builds build-local-ios
```

This runs `xcodebuild` with `-quiet` flag — expect no output until it finishes.
Takes ~10-15 minutes on M-series Macs.

The build output (`.app` bundle) lands in:
```
packages/native/mobile-apps-engine-native-builds/dist/binaries.json
```

### Troubleshooting Build

- **Keychain error** (`SecKeychainUnlock`): The `CI` env var is set. Use `env -u CI`.
- **Signing errors**: Expected for local builds — the script handles ad-hoc signing.

## Step 3: Build JS Bundles (first time only)

These are needed once per checkout. Skip if already built.

```bash
env -u CI yarn workspace @wix/mobile-apps-engine-extra-bundle local:build
env -u CI yarn workspace @wix/mobile-apps-dependencies local:build
```

## Step 4: Run E2E Tests

```bash
cd ~/Documents/mobile-apps-engine
unset PREFIX && . ~/.nvm/nvm.sh > /dev/null && nvm use default > /dev/null
env -u CI yarn workspace @wix/wix-one-app-engine test:e2e:ios -w d <suite_files...>
```

The `-w d` flag selects the Detox worker count.

### Specific suites relevant to RNN

| Suite file | What it tests |
|-----------|---------------|
| `e2e/suites/ExternalLinks.test.js` | Deep link navigation, back button |
| `e2e/suites/ExternalLinksModes.test.js` | Deep link modes (pop to root, tab switch) |
| `e2e/suites/ExternalLinksPushNotifications.test.js` | Push notification deep links |
| `e2e/suites/ExtenalLinksWithSiteSelect.test.js` | Deep links with site selection |
| `e2e/suites/EngineUIComponents.test.js` | Error screen, UI components |
| `e2e/suites/LaunchArgs.test.js` | Launch arguments |
| `e2e/suites/LazyModules.test.js` | Lazy-loaded modules |

### Example: Run the deep-link suites

```bash
env -u CI yarn workspace @wix/wix-one-app-engine test:e2e:ios -w d \
  e2e/suites/ExternalLinks.test.js \
  e2e/suites/ExternalLinksModes.test.js \
  e2e/suites/ExternalLinksPushNotifications.test.js
```

### Troubleshooting Tests

- **`Error: No dist version found`**: The native build hasn't run or failed.
  Run step 2 again.
- **`Cannot find module .../mobile-apps-engine-extra-bundle/index.js`**:
  Run step 3 (`local:build` for extra-bundle).
- **`ENOENT .../metadata.json`**: Run step 3 (`local:build` for mobile-apps-dependencies).
- **`Unable to resolve module @rozenite/...`**: The Rozenite plugin import is broken.
  Patch `packages/wix-one-app-engine/src/rozenitePlugin/helper/rozenite-network-inspector.ts`
  to make the import optional with a no-op fallback.
- **Tests time out on `navigateBack()`**: The `navigateBack()` helper taps
  `element(by.id('pop'))` on iOS. This relies on RNN's `setBackButtonTestID` working
  correctly. If the back button's view hierarchy changed (e.g., iOS 26 Liquid Glass),
  the testID isn't applied and the element can't be found.

## Key Engine Files

| File | Purpose |
|------|---------|
| `packages/wix-one-app-engine/e2e/helpers/utils.js` | Test helpers including `navigateBack()` |
| `packages/wix-one-app-engine/demo-modules/deep-links/module.js` | Deep link route definitions |
| `packages/wix-one-app-engine/demo-modules/deep-links/Screens.js` | Deep link screen components |
| `packages/wix-one-app-engine/internalScripts/test-e2e.js` | E2E test runner script |
| `packages/native/mobile-apps-engine-native-builds/local_build_scripts/build_ios.js` | Local iOS build script |

