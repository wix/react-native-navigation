---
name: rnn-codebase
description: Navigate and work with the react-native-navigation (RNN) codebase. Use when fixing bugs, adding features, tracing command flows, understanding options resolution, or working across JS/iOS/Android layers in this repo.
---

# React Native Navigation Codebase

## Architecture Overview

RNN has three layers that mirror each other:

```
JS/TS (src/)  →  TurboModule bridge  →  iOS native (ios/)
                                     →  Android native (android/)
```

A navigation command (e.g. `push`) flows:
1. `Navigation.push()` → `Commands.ts` → processing pipeline → `NativeCommandsSender.ts`
2. TurboModule: `RNNTurboModule` (iOS) / `NavigationTurboModule.kt` (Android)
3. iOS: `RNNCommandsHandler` → `RNNViewControllerFactory` → UIKit controllers
4. Android: `Navigator` → `LayoutFactory` → View-based controllers (no Fragments)

Read [ARCHITECTURE.md](../../../ARCHITECTURE.md) for the full overview.

## Key Cross-Layer Mappings

### Layout Types → Native Controllers

| JS Layout Type | iOS Controller | Android Controller |
|----------------|---------------|-------------------|
| `component` | `RNNComponentViewController` | `ComponentViewController` |
| `stack` | `RNNStackController` (UINavigationController) | `StackController` |
| `bottomTabs` | `RNNBottomTabsController` (UITabBarController) | `BottomTabsController` |
| `sideMenu` | `RNNSideMenuViewController` (MMDrawerController) | `SideMenuController` (DrawerLayout) |
| `topTabs` | `RNNTopTabsViewController` | `TopTabsController` (ViewPager) |
| `splitView` | `RNNSplitViewController` | N/A (iOS only) |
| `externalComponent` | `RNNExternalViewController` | `ExternalComponentViewController` |

### Options → Presenters

Each controller type has a Presenter that applies options to views:

| iOS Controller | iOS Presenter | Android Presenter |
|----------------|--------------|-------------------|
| `RNNComponentViewController` | `RNNComponentPresenter` | `ComponentPresenter` |
| `RNNStackController` | `RNNStackPresenter` + `TopBarPresenter` | `StackPresenter` |
| `RNNBottomTabsController` | `RNNBottomTabsPresenter` | `BottomTabsPresenter` |
| `RNNSideMenuViewController` | `RNNSideMenuPresenter` | `SideMenuPresenter` |

### Events (same names both platforms)

| Event | Trigger |
|-------|---------|
| `RNN.ComponentDidAppear` | Screen becomes visible |
| `RNN.ComponentDidDisappear` | Screen hidden |
| `RNN.NavigationButtonPressed` | TopBar button tap |
| `RNN.BottomTabSelected` | Tab changed |
| `RNN.ModalDismissed` | Modal dismissed |
| `RNN.ScreenPopped` | Screen popped from stack |
| `RNN.CommandCompleted` | Any command finished |

## Where to Find Things

### By task: "I need to fix/change X"

| Task | JS File(s) | iOS File(s) | Android File(s) |
|------|-----------|------------|----------------|
| Command execution | `src/commands/Commands.ts` | `ios/RNNCommandsHandler.mm` | `react/NavigationTurboModule.kt` |
| Layout creation | `src/commands/LayoutTreeParser.ts` | `ios/RNNViewControllerFactory.mm` | `options/LayoutFactory.java` |
| Options processing | `src/commands/OptionsProcessor.ts` | `ios/RNNNavigationOptions.mm` | `options/Options.java` |
| Options application | — | `ios/*Presenter.mm` | `viewcontrollers/*Presenter.java` |
| TopBar | `src/interfaces/Options.ts` (TopBarOptions) | `ios/TopBarPresenter.mm`, `ios/RNNUIBarButtonItem.mm` | `views/stack/topbar/` |
| Bottom tabs | `src/interfaces/Options.ts` (BottomTabsOptions) | `ios/RNNBottomTabsPresenter.mm` | `viewcontrollers/bottomtabs/` |
| Modals | `src/commands/Commands.ts` | `ios/RNNModalManager.mm` | `viewcontrollers/modal/ModalStack.java` |
| Overlays | `src/commands/Commands.ts` | `ios/RNNOverlayManager.mm` | `viewcontrollers/overlay/OverlayManager.kt` |
| Animations | `src/interfaces/Options.ts` (AnimationOptions) | `ios/ScreenAnimationController.mm` | `viewcontrollers/stack/StackAnimator.kt` |
| React view rendering | — | `ios/RNNReactView.mm` | `react/ReactView.java` |
| Events to JS | `src/adapters/NativeEventsReceiver.ts` | `ios/RNNEventEmitter.mm` | `react/events/EventEmitter.java` |
| Component registration | `src/components/ComponentRegistry.ts` | — | — |

### By directory

- **`src/`** — JS public API, commands, processing pipeline. See [src/ARCHITECTURE.md](../../../src/ARCHITECTURE.md)
- **`ios/`** — All Obj-C/C++ native code. See [ios/ARCHITECTURE.md](../../../ios/ARCHITECTURE.md)
- **`ios/TurboModules/`** — New architecture entry points (`RNNTurboModule`, `RNNTurboManager`, `RNNTurboCommandsHandler`)
- **`android/src/main/java/com/reactnativenavigation/`** — All Java/Kotlin native code. See [android/ARCHITECTURE.md](../../../android/ARCHITECTURE.md)
- **`playground/`** — Demo app for development and E2E tests
- **`playground/src/screens/`** — Test screens exercising every feature
- **`playground/e2e/`** — Detox E2E tests

## Options Resolution Order

Options are applied in ascending priority:
1. Default options (from `Navigation.setDefaultOptions()`) — lowest priority
2. Static options (from component class or `Navigation.registerComponent`)
3. Options passed in the layout call (e.g. `push`, `setRoot`)
4. `mergeOptions()` — runtime override, highest priority

## JS Processing Pipeline (exact order)

```
API layout → OptionsCrawler.crawl() → LayoutProcessor.process()
  → LayoutTreeParser.parse() → LayoutTreeCrawler.crawl()
  → OptionsProcessor (colors, assets, custom) → NativeCommandsSender
```

## iOS Patterns

- All controllers conform to `RNNLayoutProtocol`
- `RNNBasePresenter` subclasses apply options — `applyOptionsOnInit:`, `applyOptions:`, `mergeOptions:resolvedOptions:`
- Commands run on main thread (`RCTExecuteOnMainQueue`)
- React views: `RNNReactView` wraps `RCTSurfaceHostingView` (new arch)
- Overlays use separate `UIWindow` instances (`RNNOverlayWindow`)
- `RNNReactComponentRegistry` caches React component instances

## Android Patterns

- View-based, NOT Fragment-based
- All commands dispatched via `UiThread.post()`
- `ViewController<T extends ViewGroup>` is the base — `createView()` is abstract
- `ParentController` extends `ChildController` extends `ViewController`
- Bottom tabs use `AHBottomNavigation` library
- Three root layouts in `NavigationActivity`: rootLayout, modalsLayout, overlaysLayout
- Tab attachment modes: `Together`, `OnSwitchToTab`, `AfterInitialTab`

## Development Workflow

### Playground app
- `yarn start` — Metro bundler
- `yarn xcode` — Open iOS project
- `yarn studio` — Open Android project
- `yarn pod-install` — Install iOS pods

### Testing
- `yarn test-js` — Jest unit tests
- `yarn test-unit-ios` — iOS native unit tests (XCTest)
- `yarn test-unit-android` — Android native unit tests (JUnit + Robolectric)
- `yarn test-e2e-ios-ci` / `yarn test-e2e-android-ci` — Detox E2E tests

### Building
- `yarn prepare` — Builds `src/` → `lib/` (ESM + types)
- Codegen config: `rnnavigation` in `package.json`

## Common Gotchas

- iOS uses UIKit subclasses (UINavigationController, UITabBarController); Android uses custom View hierarchy
- `splitView` is iOS-only
- Side menu: iOS uses MMDrawerController (3rd party); Android uses DrawerLayout (native)
- Options that exist in JS types may not be implemented on both platforms — check the presenter
- `passProps` are stored in JS `Store`, not sent to native (cleared before bridge crossing)
- The `lib/` folder is generated — never edit it, edit `src/` instead
