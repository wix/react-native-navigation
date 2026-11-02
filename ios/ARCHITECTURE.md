# iOS Native Architecture

The iOS implementation provides native navigation using UIKit view controllers, coordinated through a bridge module that receives commands from JavaScript.

## App Integration

### RNNAppDelegate
**Files**: `RNNAppDelegate.h/mm`

Base class that user's AppDelegate must extend. Handles React Native and navigation initialization:

```objc
// AppDelegate.h - User extends RNNAppDelegate
#import "RNNAppDelegate.h"
@interface AppDelegate : RNNAppDelegate
@end

// AppDelegate.m - User implements sourceURLForBridge
@implementation AppDelegate
- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
  return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
}
@end
```

**What RNNAppDelegate does:**
- Sets up React Native feature flags (Fabric, TurboModules, Bridgeless)
- Creates `RCTRootViewFactory` and `ReactHost`
- Calls `[ReactNativeNavigation bootstrapWithHost:]` to initialize navigation
- Handles RN version differences (0.77, 0.78, 0.79+) via compile-time macros
- **Deep linking plumbing**:
  - Implements `application:openURL:options:` and `application:continueUserActivity:restorationHandler:`; both call `-dispatchDeepLinkURL:`.
  - `-dispatchDeepLinkURL:` forwards to `+[ReactNativeNavigation dispatchDeepLinkURL:]`, which hands the URL to the shared `RNNDeepLinkDispatcher` queue (see below). If the React runtime is ready the link is delivered to `RCTLinkingManager` right away, otherwise it is enqueued.
  - `RNNDeepLinkDispatcher` observes `RCTContentDidAppearNotification` (the Fabric/bridgeless signal) to flush the queue. This solves the cold-start race where URLs (push notifications, OS link launches) arrive before `RCTLinkingManager` is listening. `RNNAppDelegate` instantiates it in `application:didFinishLaunchingWithOptions:` so the first notification is observed.
  - Subclasses can call `-dispatchDeepLinkURL:` manually from notification delegates or any other URL source; the queueing behavior is reused automatically. Under the UIScene life cycle on RN >= 0.88 this does not work for app-generated URLs, see the limitation below.

### RNNDeepLinkDispatcher
**Files**: `RNNDeepLinkDispatcher.h/mm`

The single deep-link queue shared by `RNNAppDelegate` and `RNNSceneDelegate`. It is used through the `ReactNativeNavigation` class methods `+dispatchDeepLinkURL:`, `+dispatchOpenURLContexts:inScene:` and `+dispatchUserActivity:inScene:`.

- Each entry keeps what iOS delivered: an `NSURL`, a set of `UIOpenURLContext`s (with the scene), or a browsing-web `NSUserActivity` (with the scene). Non-browsing-web activities are ignored.
- Replay rules (`deliver:`):
  - RN >= 0.88 (`RNN_RN_VERSION_88_OR_NEWER`, from `RNNReactNativeVersion.h`) in a scene app (`RCTIsSceneDelegateApp()`): `+[RCTLinkingManager scene:openURLContexts:]` (one context per call, since RN only forwards the first) and `+[RCTLinkingManager scene:continueUserActivity:]`, with the original objects.
  - Otherwise: `+[RCTLinkingManager application:openURL:options:]`.
- **Limitation**: on RN >= 0.88 in a scene app, a bare `NSURL` (from `-dispatchDeepLinkURL:`) has no `UIOpenURLContext`. React Native's app-delegate entry points return `NO` and drop it; the dispatcher logs a warning (`RCTLogWarn`). On RN < 0.88 the same call keeps working under scenes.

### RNNSceneDelegate / UIScene life cycle
**Files**: `RNNSceneDelegate.h/mm`, `RNNSceneUtils.h/mm`, `RNNReactNativeVersion.h`

Apps built with the iOS 27 SDK must adopt the UIScene life cycle. `RNNSceneDelegate` is the scene counterpart of `RNNAppDelegate`; apps subclass both. The app delegate still creates the React host (`RNNAppDelegate` is unchanged in that respect); RN 0.88's `startReactNativeWithModuleName:inWindow:connectionOptions:` is not used. Apps without `UIApplicationSceneManifest` in `Info.plist` are unaffected (`RNNUsesSceneLifecycle()`).

```objc
// SceneDelegate.h - User extends RNNSceneDelegate
#import <ReactNativeNavigation/RNNSceneDelegate.h>
@interface SceneDelegate : RNNSceneDelegate
@end
```

**What RNNSceneDelegate does** (single scene only; overrides must call `super`):
- `scene:willConnectToSession:options:` calls `+[ReactNativeNavigation attachMainWindowToScene:]`, sets `self.window` to `+[ReactNativeNavigation mainWindow]`, records `launchURLContexts` / `launchUserActivities` (`launchURL` is derived from them) and calls `-dispatchLaunchLinksFromConnectionOptions:scene:`.
- `scene:openURLContexts:` and `scene:continueUserActivity:` forward to `RNNDeepLinkDispatcher` through the `ReactNativeNavigation` class methods.
- `-dispatchLaunchLinksFromConnectionOptions:scene:` handles cold start: under scenes iOS delivers the launch link only in the connection options, not through `scene:openURLContexts:`. The default implementation queues the URL contexts and browsing-web activities, so JS receives them as `Linking` `url` events. It is overridable. Note that `Linking.getInitialURL()` returns `null` under scenes, because the React host is created before the scene connects.

**Main window**: `+[ReactNativeNavigation mainWindow]` returns `delegate.window` (creating and assigning one if needed), so libraries that read `delegate.window` keep working. `attachMainWindowToScene:` moves that window to the scene (or creates it with `initWithWindowScene:`) and makes it key and visible.

**Scene-aware windows** (`RNNSceneUtils`): `RNNMainWindowScene()`, `RNNKeyWindow()`, `RNNStatusBarFrame()` and `+[UIWindow rnn_windowInMainWindowSceneWithFrame:]`. Overlay windows (`RNNOverlayManager`) and bottom-tab preload windows are created in the main window's scene; a window created with `initWithFrame:` never appears under scenes.

### ReactNativeNavigation Bootstrap
**File**: `ReactNativeNavigation.h/mm`

Public API for native initialization:

```objc
// New architecture (0.77+)
[ReactNativeNavigation bootstrapWithHost:reactHost];

// Legacy bridge
[ReactNativeNavigation bootstrapWithBridge:bridge];

// Register native screens
[ReactNativeNavigation registerExternalComponent:@"NativeScreen"
                                        callback:^(NSDictionary *props, RCTBridge *bridge) {
    return [[MyNativeVC alloc] init];
}];
```

### Autolink Script (`npx rnn-link`)

The `autolink/postlink/postLinkIOS.js` script automates setup:

| Linker | What it does |
|--------|--------------|
| `AppDelegateLinker` | Changes AppDelegate to extend `RNNAppDelegate`, imports ReactNativeNavigation, removes RCTRootView setup |
| `PodfileLinker` | Adds required pods configuration |
| `PlistLinker` | Updates Info.plist if needed |

**AppDelegateLinker transformations:**
- Swift: `class AppDelegate: RCTAppDelegate` → `class AppDelegate: RNNAppDelegate`
- Obj-C header: `@interface AppDelegate : RCTAppDelegate` → `@interface AppDelegate : RNNAppDelegate`
- Imports `<ReactNativeNavigation/ReactNativeNavigation.h>`
- Removes manual RCTRootView/window setup (navigation manages the window)

## Directory Structure

```
ios/
├── RNNBridgeModule.h/mm         # Bridge entry point (legacy architecture)
├── RNNBridgeManager.h/mm        # Bridge initialization
├── RNNCommandsHandler.h/mm      # Command dispatcher
├── RNNEventEmitter.h/mm         # Event emission to JS
├── RNNLayoutManager.h/mm        # View controller tracking
├── RNNLayoutNode.h/mm           # Layout tree parsing
├── RNNNavigationOptions.h/mm    # Options model
├── RNNComponentViewController.h/mm  # React component screen
├── RNNStackController.h/mm      # Stack navigation
├── RNNBottomTabsController.h/mm # Tab navigation
├── RNNSideMenuViewController.h/mm   # Side menu
├── RNNTopTabsViewController.h/mm    # Top tabs
├── RNNSplitViewController.h/mm  # Split view (iPad)
├── RNNModalManager.h/mm         # Modal presentation
├── RNNOverlayManager.h/mm       # Overlay management
├── RNNSceneDelegate.h/mm        # Base scene delegate (UIScene life cycle)
├── RNNSceneUtils.h/mm           # Scene-aware window creation and key window helpers
├── RNNDeepLinkDispatcher.h/mm   # Shared deep-link queue and replay into RCTLinkingManager
├── RNNReactNativeVersion.h      # Compile-time RN >= 0.88 gate
├── TurboModules/                # New architecture entry points
│   ├── RNNTurboModule.h/mm      # TurboModule spec implementation
│   ├── RNNTurboManager.h/mm     # Manager for host, window, external components
│   ├── RNNTurboCommandsHandler.h/mm  # TurboModule command routing
│   └── RNNTurboEventEmitter.h/mm     # Event emission for new arch
├── Utils/                       # Utility classes
├── RNNSideMenu/                 # MMDrawerController integration
└── ReactNativeNavigation.xcodeproj/
```

## Bridge Architecture

The library supports both the legacy bridge and new TurboModule architecture:
- **Legacy (Bridge)**: `RNNBridgeModule` receives commands via `RCT_EXPORT_METHOD`
- **New Architecture**: `RNNTurboModule` (in `TurboModules/`) receives commands directly

Both entry points delegate to `RNNCommandsHandler` for command execution.

```
┌────────────────────────────────────────────────────────────┐
│                 JavaScript (TurboModule)                   │
└─────────────────────────┬──────────────────────────────────┘
                          │
┌─────────────────────────▼──────────────────────────────────┐
│  RNNBridgeModule (bridge) / RNNTurboModule (new arch)      │
│  - setRoot, push, pop, showModal, dismissModal, etc.       │
└─────────────────────────┬──────────────────────────────────┘
                          │
┌─────────────────────────▼──────────────────────────────────┐
│  RNNCommandsHandler                                         │
│  - Validates commands, manages layout lifecycle            │
│  - Coordinates with managers                               │
└──────┬──────────────┬──────────────┬──────────────┬────────┘
       │              │              │              │
┌──────▼─────┐ ┌──────▼─────┐ ┌──────▼─────┐ ┌─────▼──────┐
│RNNLayout   │ │RNNModal    │ │RNNOverlay  │ │RNNViewController│
│Manager     │ │Manager     │ │Manager     │ │Factory     │
└────────────┘ └────────────┘ └────────────┘ └────────────┘
```

## View Controller Hierarchy

All navigation view controllers conform to `RNNLayoutProtocol`:

```
UIViewController
├── RNNComponentViewController    # React component screen
├── RNNExternalViewController     # Native screen wrapper
└── RNNSplashScreenViewController # Launch screen

UINavigationController
└── RNNStackController           # Stack navigation

UITabBarController
└── RNNBottomTabsController      # Bottom tabs

UIViewController (custom)
├── RNNTopTabsViewController     # Horizontal top tabs
└── RNNSideMenuChildViewController  # Drawer child

MMDrawerController
└── RNNSideMenuViewController    # Side menu/drawer

UISplitViewController
└── RNNSplitViewController       # Master-detail (iPad)
```

## Core Classes

### RNNBridgeModule
Entry point for JavaScript commands (legacy architecture). Exports methods via `RCT_EXPORT_METHOD`:

```objc
RCT_EXPORT_METHOD(setRoot:(NSString*)commandId layout:(NSDictionary*)layout
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject);
```

`RNNBridgeModule` returns the main queue as its `methodQueue`; `RNNTurboModule` uses `RCTExecuteOnMainQueue` for each command to ensure UI operations run on the main thread.

### RNNBridgeManager
Initializes bridge infrastructure:
- Creates RNNLayoutManager, RNNModalManager, RNNOverlayManager
- Creates RNNViewControllerFactory, RNNCommandsHandler
- Provides extra modules to React bridge
- Handles JavaScript reload events

### RNNCommandsHandler
Central command dispatcher implementing all navigation operations:
- `setRoot:` - Replace root view controller
- `push:componentId:layout:` - Push to stack
- `pop:componentId:` - Pop from stack
- `showModal:` - Present modal
- `dismissModal:` - Dismiss modal
- `showOverlay:` - Show overlay window
- `dismissOverlay:` - Hide overlay

### RNNLayoutManager
Tracks active view controllers:
- `addPendingViewController:` - Register during creation
- `removePendingViewController:` - Cleanup after presentation
- `findComponentForId:` - Lookup by componentId

### RNNLayoutNode
Parses JSON layout from JavaScript:
- Determines type via predicates: `isComponent`, `isStack`, `isTabs`, etc.
- Holds `type`, `nodeId`, `data` (options), `children`

### RNNViewControllerFactory
Creates view controllers from RNNLayoutNode:
- `createStack:` → RNNStackController
- `createBottomTabs:` → RNNBottomTabsController
- `createComponent:` → RNNComponentViewController
- `createSideMenu:` → RNNSideMenuViewController

## RNNLayoutProtocol

Protocol all navigation controllers implement:

```objc
@protocol RNNLayoutProtocol
- (instancetype)initWithLayoutInfo:(RNNLayoutInfo*)layoutInfo
                           creator:(id<RNNComponentViewCreator>)creator
                           options:(RNNNavigationOptions*)options
                    defaultOptions:(RNNNavigationOptions*)defaultOptions
                         presenter:(RNNBasePresenter*)presenter
                      eventEmitter:(RNNEventEmitter*)eventEmitter
              childViewControllers:(NSArray*)childViewControllers;

- (void)render;
- (UIViewController*)getCurrentChild;
- (void)mergeOptions:(RNNNavigationOptions*)options;
- (RNNNavigationOptions*)resolveOptions;
- (void)setDefaultOptions:(RNNNavigationOptions*)defaultOptions;
- (void)onChildWillAppear;
- (void)readyForPresentation;
- (CGFloat)getTopBarHeight;
- (CGFloat)getBottomTabsHeight;
@end
```

Additional methods provided via `UIViewController+LayoutProtocol` category:
- `destroy`, `topMostViewController`, `stack`
- `componentWillAppear`, `componentDidAppear`, `componentDidDisappear`
- `screenPopped`, `prepareForTransition`
- `resolveOptionsWithDefault`, `mergeChildOptions:child:`

## Presenter Pattern

Each view controller type has a corresponding presenter that applies options:

| Controller | Presenter |
|------------|-----------|
| RNNComponentViewController | RNNComponentPresenter |
| RNNStackController | RNNStackPresenter + TopBarPresenter |
| RNNBottomTabsController | RNNBottomTabsPresenter + BottomTabPresenter |
| RNNSideMenuViewController | RNNSideMenuPresenter |
| RNNSplitViewController | RNNSplitViewControllerPresenter |

### RNNBasePresenter
Base class with lifecycle methods:
- `applyOptionsOnInit:` - Initial setup
- `applyOptions:` - Apply current options
- `mergeOptions:resolvedOptions:` - Runtime updates
- `componentWillAppear`, `componentDidAppear`, `componentDidDisappear`

## React View Integration

### RNNReactView
Wraps RCTRootView (legacy) or RCTSurfaceHostingView (new architecture):
- Implements `RNNComponentProtocol`
- Manages `componentId`, `componentType`, `moduleName`
- Lifecycle: `componentWillAppear`, `componentDidAppear`, `componentDidDisappear`

### RNNReactRootViewCreator
Creates RNNReactView instances:
- Supports component types: Component, TopBarTitle, TopBarButton, TopBarBackground
- Handles size flexibility for flexible layouts

### RNNReactComponentRegistry
Caches created React component instances:
- `createComponentIfNotExists:parentComponentId:componentType:reactViewReadyBlock:`
- `removeComponent:`, `clearComponentsForParentId:`

## Options System

### RNNNavigationOptions
Master options object containing all configuration:

```objc
@interface RNNNavigationOptions : RNNOptions
@property RNNTopBarOptions* topBar;
@property RNNBottomTabsOptions* bottomTabs;
@property RNNBottomTabOptions* bottomTab;
@property RNNTopTabsOptions* topTabs;
@property RNNSideMenuOptions* sideMenu;
@property RNNOverlayOptions* overlay;
@property RNNAnimationsOptions* animations;
@property RNNStatusBarOptions* statusBar;
@property RNNLayoutOptions* layout;
@property RNNModalOptions* modal;
@property RNNPreviewOptions* preview;
@property RNNSplitViewOptions* splitView;
@end
```

### Options Resolution
Options merge in hierarchy:
1. Component's own options
2. Parent controller options (loop through chain)
3. Default options (from `setDefaultOptions`)

## Event Emission

### RNNEventEmitter
Sends events to JavaScript via RCTEventEmitter:

| Event | Method |
|-------|--------|
| Component lifecycle | `sendComponentWillAppear:`, `sendComponentDidAppear:`, `sendComponentDidDisappear:` |
| Button press | `sendOnNavigationButtonPressed:buttonId:` |
| Command completion | `sendOnNavigationCommandCompletion:commandId:` |
| Tab events | `sendBottomTabSelected:unselected:`, `sendBottomTabLongPressed:`, `sendBottomTabPressed:` |
| Modal events | `sendModalsDismissedEvent:numberOfModalsDismissed:`, `sendModalAttemptedToDismissEvent:` |
| Screen events | `sendScreenPoppedEvent:` |
| Search | `sendOnSearchBarUpdated:text:isFocused:`, `sendOnSearchBarCancelPressed:` |
| Preview | `sendOnPreviewCompleted:previewComponentId:` |

### RNNBridgeEventEmitter
Concrete implementation that sends `onAppLaunched` on initialization.

## Modal & Overlay Management

### RNNModalManager
- Tracks presented modals array
- Supports custom transition animations via ScreenAnimationController
- Handles presentationController delegate for adaptive presentation

### RNNOverlayManager
- Manages UIWindow instances for overlays
- Each overlay gets its own RNNOverlayWindow
- Maintains previous window reference for restoration
- Controls window level and accessibility

## Animation System

### ScreenAnimationController
Implements `UIViewControllerAnimatedTransitioning`:
- Custom push/pop/modal transitions
- Content transitions (RNNEnterExitAnimation)
- Element transitions (ElementTransitionOptions)
- Shared element transitions (SharedElementTransitionOptions)

### Element Animators
- `ElementAnimator` - Individual element animations
- `SharedElementAnimator` - Cross-screen shared elements
- Transition types: Alpha, Scale, Translation, Frame, Bounds, Color, CornerRadius

### RNNSetRootAnimator
Animates window root replacement using CABasicAnimation.

## Navigation Types

### Stack Navigation (RNNStackController)
- Subclass of UINavigationController
- Push/pop with custom animations
- Back button customization
- Uses StackControllerDelegate for bar delegate handling

```objc
// Extension methods
@interface UINavigationController (RNNCommands)
- (void)push:(UIViewController*)vc onTop:(UIViewController*)onTopVC
     animated:(BOOL)animated completion:(void(^)(void))completion rejection:(RCTPromiseRejectBlock)rejection;
- (void)popAnimated:(BOOL)animated completion:(void(^)(NSString*))completion rejection:(RCTPromiseRejectBlock)rejection;
@end
```

### Bottom Tabs (RNNBottomTabsController)
- Subclass of UITabBarController
- Tab attachment modes: Together, OnSwitchToTab, AfterInitialTab
- Long-press gesture support
- Badge and dot indicator support

### Side Menu (RNNSideMenuViewController)
- Uses MMDrawerController (third-party)
- Center, left, right child containers
- Configurable open modes and widths

### Top Tabs (RNNTopTabsViewController)
- Custom horizontal tab implementation
- Not based on UITabBarController
- Manual content switching

### Split View (RNNSplitViewController)
- UISplitViewController subclass
- Master-detail for iPad

## External Components

### RNNExternalComponentStore
Registry for native (non-React) view controllers.

### RNNExternalViewController
Wraps native UIViewController for integration:

```objc
[ReactNativeNavigation registerExternalComponent:@"NativeScreen"
                                       callback:^(NSDictionary *props, RCTBridge *bridge) {
    return [[MyNativeViewController alloc] init];
}];
```

## Key Files Reference

| File | Purpose |
|------|---------|
| `RNNBridgeModule.h/mm` | Bridge entry point (legacy) |
| `TurboModules/RNNTurboModule.mm` | TurboModule entry point (new arch) |
| `TurboModules/RNNTurboCommandsHandler.mm` | TurboModule command routing |
| `RNNCommandsHandler.h/mm` | Command execution |
| `RNNLayoutManager.h/mm` | Controller tracking |
| `RNNViewControllerFactory.h/mm` | Controller creation |
| `RNNComponentViewController.h/mm` | React screen |
| `RNNStackController.h/mm` | Stack navigation |
| `RNNBottomTabsController.h/mm` | Tab navigation |
| `RNNNavigationOptions.h/mm` | Options model |
| `RNNBasePresenter.h/mm` | Presenter base |
| `TopBarPresenter.h/mm` | Top bar styling |
| `RNNReactView.h/mm` | React view wrapper |
| `ScreenAnimationController.h/mm` | Custom transitions |
