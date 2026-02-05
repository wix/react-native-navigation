# iOS Native Architecture

The iOS implementation provides native navigation using UIKit view controllers, coordinated through a bridge module that receives commands from JavaScript.

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
