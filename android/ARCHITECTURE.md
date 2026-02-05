# Android Native Architecture

The Android implementation provides native navigation using Views and ViewGroups (not Fragments), coordinated through a TurboModule that receives commands from JavaScript.

## App Integration

### NavigationApplication
**File**: `NavigationApplication.java`

Abstract Application class that user's MainApplication must extend:

```kotlin
// MainApplication.kt - User extends NavigationApplication
class MainApplication : NavigationApplication() {
    override val reactNativeHost: ReactNativeHost
        get() = NavigationReactNativeHost(this) // Must use NavigationReactNativeHost
}
```

**What NavigationApplication does:**
- Initializes SoLoader
- Provides `ReactGateway` singleton for React lifecycle
- Offers `registerExternalComponent()` for native screens

### NavigationActivity
**File**: `NavigationActivity.java`

Base Activity that user's MainActivity must extend:

```kotlin
// MainActivity.kt - User extends NavigationActivity
class MainActivity : NavigationActivity() {
    // No getMainComponentName() needed - navigation handles root
}
```

**What NavigationActivity does:**
- Creates `Navigator` with root, modal, and overlay layouts
- Manages back press via `OnBackPressedCallback`
- Coordinates React lifecycle with activity lifecycle

### NavigationReactNativeHost
**File**: `react/NavigationReactNativeHost.kt`

Custom ReactNativeHost that integrates with navigation:
- Used instead of `DefaultReactNativeHost`
- Provides proper module registration for navigation

### Autolink Script (`npx rnn-link`)

The `autolink/postlink/postLinkAndroid.js` script automates setup:

| Linker | What it does |
|--------|--------------|
| `ApplicationLinker` | Changes MainApplication to extend `NavigationApplication`, uses `NavigationReactNativeHost`, removes SoLoader.init() |
| `ActivityLinker` | Changes MainActivity to extend `NavigationActivity`, removes `getMainComponentName()` |
| `GradleLinker` | Updates build.gradle if needed |

**ApplicationLinker transformations:**
- `class MainApplication : Application(), ReactApplication` → `class MainApplication : NavigationApplication()`
- `DefaultReactNativeHost(this)` → `NavigationReactNativeHost(this)`
- Removes `SoLoader.init()` (NavigationApplication handles it)
- Removes new architecture entry point load block

**ActivityLinker transformations:**
- `class MainActivity : ReactActivity()` → `class MainActivity : NavigationActivity()`
- Removes `getMainComponentName()` override
- Removes `createReactActivityDelegate()` override

## Directory Structure

```
android/src/main/java/com/reactnativenavigation/
├── NavigationActivity.java       # Main Activity entry point
├── NavigationApplication.java    # Application class
├── NavigationPackage.kt          # React package registration
├── react/                        # React bridge layer
│   ├── NavigationTurboModule.kt  # TurboModule implementation
│   ├── ReactGateway.java         # React lifecycle management
│   └── events/
│       └── EventEmitter.java     # Event emission to JS
├── options/                      # Options parsing and factories
│   ├── LayoutFactory.java        # Creates ViewControllers from layout nodes
│   └── Options.java              # Options model
├── viewcontrollers/              # ViewController hierarchy
│   ├── ViewController.java       # Base class
│   ├── ChildController.java      # With presenter support
│   ├── ParentController.java     # Container controllers
│   ├── stack/                    # Stack navigation
│   ├── bottomtabs/               # Bottom tabs
│   ├── toptabs/                  # Top tabs (lowercase)
│   ├── sidemenu/                 # Side menu/drawer
│   ├── modal/                    # Modal management
│   └── externalcomponent/        # Native component support
├── views/                        # UI View implementations
├── utils/                        # Utilities
└── hierarchy/                    # Root layout management
```

## Bridge Architecture

```
┌────────────────────────────────────────────────────────────┐
│                 JavaScript (TurboModule)                   │
└─────────────────────────┬──────────────────────────────────┘
                          │
┌─────────────────────────▼──────────────────────────────────┐
│  NavigationTurboModule.kt                                   │
│  - setRoot, push, pop, showModal, dismissModal, etc.       │
└─────────────────────────┬──────────────────────────────────┘
                          │
┌─────────────────────────▼──────────────────────────────────┐
│  Navigator (Root ViewController)                           │
│  - Manages root, modals, overlays                          │
│  - Coordinates LayoutFactory                               │
└──────┬──────────────┬──────────────┬──────────────┬────────┘
       │              │              │              │
┌──────▼─────┐ ┌──────▼─────┐ ┌──────▼─────┐ ┌─────▼──────┐
│ModalStack  │ │OverlayMgr  │ │LayoutFactory│ │EventEmitter│
└────────────┘ └────────────┘ └────────────┘ └────────────┘
```

## Entry Points

### NavigationApplication
Abstract Application class that apps extend:
- Manages `ReactGateway` singleton
- Initializes SoLoader
- Provides `registerExternalComponent()` for native screens

### NavigationActivity
Main Activity containing all navigation:
- Creates `Navigator` with three CoordinatorLayouts:
  - `rootLayout` - Main navigation content
  - `modalsLayout` - Modal stack
  - `overlaysLayout` - Overlays (highest z-order)
- Handles back press via `OnBackPressedCallback`
- Coordinates activity lifecycle with React

### NavigationTurboModule
TurboModule implementation (Kotlin, ~300 lines):

```kotlin
@ReactMethod
fun setRoot(commandId: String, layout: ReadableMap, promise: Promise)

@ReactMethod
fun push(commandId: String, componentId: String, layout: ReadableMap, promise: Promise)

@ReactMethod
fun showModal(commandId: String, layout: ReadableMap, promise: Promise)
```

All commands execute on UI thread via `UiThread.post()`.

## ViewController Hierarchy

**Note**: Android implementation does NOT use Fragments - it's purely View-based.

```
ViewController (abstract)
│   - Base for all navigation controllers
│   - Manages view creation and lifecycle
│   - Handles options and back press
│
├── ChildController
│   │   - Adds Presenter support
│   │
│   ├── ComponentViewController
│   │       - Renders React components via ReactView
│   │
│   └── ExternalComponentViewController
│           - Wraps native Android views
│
└── ParentController (extends ChildController)
        - Container for child controllers
        │
        ├── StackController
        │       - Push/pop navigation
        │       - Uses IdStack<ViewController>
        │
        ├── BottomTabsController
        │       - UITabBarController equivalent
        │       - Uses AHBottomNavigation
        │
        ├── TopTabsController
        │       - Horizontal ViewPager tabs
        │
        ├── SideMenuController
        │       - DrawerLayout-based drawer
        │
        └── Navigator
                - Root controller
                - Manages modals and overlays
```

## Core Classes

### ViewController (Base)
**File**: `viewcontrollers/viewcontroller/ViewController.java` (~500 lines)

Abstract base for all navigation controllers:

```java
public abstract class ViewController<T extends ViewGroup> {
    protected abstract T createView();  // Subclasses create views

    public void onViewWillAppear() { }
    public void onViewDidAppear() { }
    public void onViewDisappear() { }

    public void mergeOptions(Options options) { }
    public void applyOptions(Options options) { }
    public boolean handleBack(CommandListener listener) { }
    public void destroy() { }
}
```

### Navigator (Root)
**File**: `viewcontrollers/navigator/Navigator.java`

Root ViewController managing:
- Root content hierarchy
- Modal stack (`ModalStack`)
- Overlay manager (`OverlayManager`)
- Component lookup by ID

### LayoutFactory
**File**: `options/LayoutFactory.java`

Factory creating ViewControllers from LayoutNode. Owned by `NavigationTurboModule` and used to build the view controller hierarchy when commands are received from JavaScript.

```java
public ViewController create(LayoutNode node) {
    switch (node.type) {
        case Component: return createComponent(node);
        case Stack: return createStack(node);
        case BottomTabs: return createBottomTabs(node);
        case SideMenuRoot: return createSideMenuRoot(node);
        case SideMenuCenter: return createSideMenuContent(node);
        case SideMenuLeft: return createSideMenuLeft(node);
        case SideMenuRight: return createSideMenuRight(node);
        case TopTabs: return createTopTabs(node);
        // Note: SplitView is iOS-only, not implemented on Android
    }
}
```

## Navigation Types

### Stack Navigation (StackController)
**Files**: `viewcontrollers/stack/`

- View: `StackLayout` (CoordinatorLayout with TopBar)
- Internal stack: `IdStack<ViewController<?>>`
- Components: `TopBarController`, `StackAnimator`, `StackPresenter`, `FabPresenter`

Operations:
- `push(viewController, listener)`
- `pop(options, listener)`
- `popTo(target, options, listener)`
- `popToRoot(options, listener)`
- `setRoot(children, listener)`

### Bottom Tabs (BottomTabsController)
**Files**: `viewcontrollers/bottomtabs/`

- View: `BottomTabsLayout` (CoordinatorLayout with BottomTabs)
- Uses `AHBottomNavigation` library
- Components: `BottomTabsPresenter`, `BottomTabPresenter`, `BottomTabsAnimator`

Attachment Modes:
- `TogetherAttacher` - Load all tabs immediately
- `OnSwitchToTabAttacher` - Lazy load on selection
- `AfterInitialTabAttacher` - Load initial + others in background

### Side Menu (SideMenuController)
**Files**: `viewcontrollers/sidemenu/`

- View: `SideMenuRoot` (DrawerLayout-based)
- Children: Center, Left, Right controllers
- `SideMenuPresenter` for styling

### Top Tabs (TopTabsController)
**Files**: `viewcontrollers/toptabs/`

- View: `TopTabsViewPager` (ViewPager-based)
- `TopTabsLayoutCreator` for view creation

### Modals (ModalStack)
**Files**: `viewcontrollers/modal/`

Not a ViewController - manages modal presentation:
- `ModalPresenter` - Show/dismiss with animations
- `ModalAnimator` (Kotlin) - Animation handling
- Stack of modals with lifecycle management

### Overlays (OverlayManager)
**File**: `viewcontrollers/overlay/OverlayManager.kt`

Registry of active overlays:
- Show/dismiss by component ID
- Configuration change handling
- Host pause/resume lifecycle

## React Component Rendering

### ComponentViewController
Manages React component lifecycle:
- Creates `ComponentLayout` view
- Initializes `ReactView` with ReactSurface
- Emits lifecycle events to JS
- Handles scroll events and status bar

### ReactView
**File**: `react/ReactView.java`

Extends FrameLayout, wraps `ReactSurface`:
- `start()` - Attach surface to React instance
- `destroy()` - Clean up React surface
- `sendComponentWillStart()` - Emit event to JS
- `isReady()` - Check surface attachment

## Event Emission

### EventEmitter
**File**: `react/events/EventEmitter.java`

Events sent to JavaScript:

| Event | Description |
|-------|-------------|
| `RNN.AppLaunched` | App initialization complete |
| `RNN.ComponentDidAppear` | Component visible |
| `RNN.ComponentWillAppear` | About to become visible |
| `RNN.ComponentDidDisappear` | Component hidden |
| `RNN.NavigationButtonPressed` | TopBar button tapped |
| `RNN.BottomTabSelected` | Tab selection changed |
| `RNN.BottomTabPressed` | Tab pressed (even if selected) |
| `RNN.ModalDismissed` | Modal was dismissed |
| `RNN.ScreenPopped` | Screen removed from stack |
| `RNN.CommandCompleted` | Navigation command finished |

## Options System

### Options Class
**File**: `options/Options.java`

```java
public class Options {
    public TopBarOptions topBar;
    public TopTabsOptions topTabs;
    public BottomTabsOptions bottomTabsOptions;
    public BottomTabOptions bottomTabOptions;
    public OverlayOptions overlayOptions;
    public FabOptions fabOptions;
    public AnimationsOptions animations;
    public SideMenuRootOptions sideMenuRootOptions;
    public ModalOptions modal;
    public StatusBarOptions statusBar;
    public NavigationBarOptions navigationBar;
    public LayoutOptions layout;
    public HardwareBackButtonOptions hardwareBack;
}
```

### Options Flow
1. **Parse**: JSON from JS → Options object via JSONParser
2. **Merge**: Default → Screen → Component options
3. **Apply**: Presenter applies to views

## Presenter Pattern

Each ViewController has a Presenter:

| ViewController | Presenter |
|---------------|-----------|
| StackController | StackPresenter |
| BottomTabsController | BottomTabsPresenter + BottomTabPresenter |
| ComponentViewController | ComponentPresenter |
| SideMenuController | SideMenuPresenter |
| Navigator | RootPresenter |

### Presenter Base
**File**: `viewcontrollers/viewcontroller/Presenter.java`

Handles:
- Orientation changes
- Status bar styling
- Navigation bar styling
- Layout parameters

## Animation System

### Animators (Kotlin)
- `StackAnimator.kt` - Push, pop, setRoot animations
- `ModalAnimator.kt` - Modal show/dismiss
- `BottomTabsAnimator.kt` - Tab transitions
- `TopBarAppearanceAnimator.kt` - Top bar changes

### Element Transitions
Via `TransitionAnimatorCreator.kt`:
- Position, scale, rotation animations
- Color transitions
- Corner radius animations
- Shared element support

## Activity Lifecycle

```java
onCreate()      // Create Navigator, initialize React
onPostCreate()  // Set content layout
onResume()      // React host resume, event emitter setup
onPause()       // React host pause
onDestroy()     // Clean up Navigator
onConfigurationChanged()  // Handle orientation, locale
onNewIntent()   // Process deep links
onActivityResult()  // Handle permissions
```

## ViewController Lifecycle

```
View creation
    ↓
onViewWillAppear()   // Register with registry
    ↓
onViewDidAppear()    // Emit event to JS
    ↓
[visible]
    ↓
onViewWillDisappear()
    ↓
onViewDisappear()    // Unregister from registry
    ↓
destroy()            // Complete cleanup
```

## Key Files Reference

All paths relative to `android/src/main/java/com/reactnativenavigation/`:

| File | Path | Purpose |
|------|------|---------|
| NavigationActivity.java | `.` | Main Activity |
| NavigationApplication.java | `.` | Application class |
| NavigationTurboModule.kt | `react/` | JS bridge |
| Navigator.java | `viewcontrollers/navigator/` | Root controller |
| ViewController.java | `viewcontrollers/viewcontroller/` | Base class |
| StackController.java | `viewcontrollers/stack/` | Stack navigation |
| BottomTabsController.java | `viewcontrollers/bottomtabs/` | Tab navigation |
| LayoutFactory.java | `options/` | Controller factory |
| Options.java | `options/` | Options model |
| Presenter.java | `viewcontrollers/viewcontroller/` | Base presenter |
| EventEmitter.java | `react/events/` | JS events |
| ReactView.java | `react/` | React component wrapper |
| StackAnimator.kt | `viewcontrollers/stack/` | Stack animations |
| ModalStack.java | `viewcontrollers/modal/` | Modal management |
