# JavaScript/TypeScript Architecture

The JavaScript layer provides the public API and orchestrates communication between React components and native navigation implementations.

## Entry Point

`index.ts` exports the `Navigation` singleton (a `NavigationDelegate` instance) along with re-exports from `Modal`, `EventsRegistry`, `Constants`, and all interfaces. Applications use `Navigation.*` for commands and `Navigation.events().*` for event subscriptions.

## Module Structure

```
src/
├── index.ts                 # Main entry point, exports Navigation singleton
├── Navigation.ts            # NavigationRoot - core orchestrator
├── NavigationDelegate.ts    # Public API facade
├── adapters/                # Native bridge layer
├── commands/                # Command processing
├── components/              # React component management
├── events/                  # Event system
├── interfaces/              # TypeScript type definitions
└── processors/              # Extensibility hooks
```

## Core Classes

### NavigationDelegate (Public API)
**File**: `NavigationDelegate.ts`

Facade providing the public `Navigation` API. All methods proxy to `NavigationRoot`.

```typescript
// Usage
import { Navigation } from 'react-native-navigation';
Navigation.setRoot({ root: { stack: { children: [...] } } });
```

### NavigationRoot (Core Orchestrator)
**File**: `Navigation.ts`

Coordinates all subsystems. Constructed with dependencies:
- `NativeCommandsSender` - sends commands to native
- `NativeEventsReceiver` - receives events from native
- `AppRegistryService` - manages React Native component registry

Initializes and coordinates:
- `Store` - component instance & props storage
- `ComponentRegistry` - component registration
- `LayoutTreeParser` - converts layouts to internal nodes
- `LayoutTreeCrawler` - processes layout trees
- `Commands` - command execution
- `EventsRegistry` - event subscriptions
- `OptionsProcessor` - option processing
- `LayoutProcessor` - layout transformations

## Adapters Layer

Bridge between JavaScript and native/React Native APIs.

| Adapter | Purpose |
|---------|---------|
| `NativeCommandsSender` | Wraps TurboModule for command dispatch |
| `NativeEventsReceiver` | Wraps NativeEventEmitter for events |
| `NativeRNNTurboModule` | TurboModule spec interface |
| `NativeRNNTurboEventEmitter` | TurboModule event emitter spec |
| `UniqueIdProvider` | Generates unique component/command IDs |
| `ColorService` | Converts colors to native format |
| `AssetResolver` | Resolves require() image assets (class: AssetService) |
| `AppRegistryService` | Registers components with React Native |
| `Constants` | Platform dimension constants |
| `TouchablePreview` | 3D Touch / Haptic preview handling |

## Commands Layer

### Commands Class
**File**: `commands/Commands.ts`

Central command dispatcher. Each navigation command:
1. Validates input
2. Processes layout through pipeline
3. Sends to native via adapter
4. Returns promise with result

### Processing Pipeline

The pipeline executes in this exact order:

```
Input Layout (from API)
       ↓
1. OptionsCrawler.crawl()         # Extract static options from component classes
       ↓
2. LayoutProcessor.process()      # Apply registered layout processors
       ↓
3. LayoutTreeParser.parse()       # Convert to internal LayoutNode tree
       ↓
4. LayoutTreeCrawler.crawl()      # Process options, save props to Store
       ↓
5. OptionsProcessor (during crawl) # Resolve colors, assets, custom processors
       ↓
6. NativeCommandsSender           # Send to native module
```

### LayoutNode (Internal Tree)
```typescript
interface LayoutNode {
  id: string;           // Unique identifier
  type: LayoutType;     // Component|Stack|BottomTabs|etc
  data: {
    name?: string;      // Component name
    options?: any;      // Processed options
    passProps?: any;    // Component props (cleared before native)
  };
  children: LayoutNode[];
}
```

### LayoutType Enum
```typescript
enum LayoutType {
  Component = 'Component',
  Stack = 'Stack',
  BottomTabs = 'BottomTabs',
  SideMenuRoot = 'SideMenuRoot',
  SideMenuCenter = 'SideMenuCenter',
  SideMenuLeft = 'SideMenuLeft',
  SideMenuRight = 'SideMenuRight',
  TopTabs = 'TopTabs',
  ExternalComponent = 'ExternalComponent',
  SplitView = 'SplitView',
}
```

## Components Layer

### ComponentRegistry
**File**: `components/ComponentRegistry.ts`

Manages React component registration and wrapping.

```typescript
Navigation.registerComponent('ScreenName', () => MyComponent);
```

### ComponentWrapper
**File**: `components/ComponentWrapper.tsx`

Higher-order component that:
- Wraps original component with lifecycle management
- Stores instance in `Store` for event dispatch
- Injects `componentId` and `componentName` props

### Store
**File**: `components/Store.ts`

Centralized storage for:
- `componentsByName` - registered component providers
- `componentsInstancesById` - mounted component instances
- `propsById` - static props by component ID
- `pendingPropsById` - props awaiting component mount
- `wrappedComponents` - cached wrapped components
- `lazyRegistratorFn` - function for lazy component registration

## Events Layer

### EventsRegistry
**File**: `events/EventsRegistry.ts`

Public API for event subscriptions:

```typescript
Navigation.events().registerComponentDidAppearListener(({ componentId }) => {});
Navigation.events().registerCommandCompletedListener(({ commandId }) => {});
```

### ComponentEventsObserver
**File**: `events/ComponentEventsObserver.ts`

Dispatches events to component instance methods:
- `componentWillAppear()`
- `componentDidAppear()`
- `componentDidDisappear()`
- `navigationButtonPressed()`
- `screenPopped()`

### CommandsObserver
**File**: `events/CommandsObserver.ts`

Notifies listeners of command lifecycle (start, complete).

## Events (Native → JS)

| Event | Description |
|-------|-------------|
| `RNN.AppLaunched` | App initialization complete |
| `RNN.ComponentWillAppear` | Component about to appear |
| `RNN.ComponentDidAppear` | Component now visible |
| `RNN.ComponentDidDisappear` | Component hidden |
| `RNN.NavigationButtonPressed` | TopBar button tapped |
| `RNN.BottomTabPressed` | Tab pressed (even if selected) |
| `RNN.BottomTabSelected` | Tab selection changed |
| `RNN.BottomTabLongPressed` | Tab long-pressed |
| `RNN.ModalDismissed` | Modal was dismissed |
| `RNN.ModalAttemptedToDismiss` | Swipe-to-dismiss attempted |
| `RNN.ScreenPopped` | Screen removed from stack |
| `RNN.SearchBarUpdated` | Search text changed |
| `RNN.SearchBarCancelPressed` | Search cancelled |
| `RNN.PreviewCompleted` | 3D Touch preview completed |
| `RNN.CommandCompleted` | Navigation command finished |

## Processors Layer

### OptionProcessorsStore
**File**: `processors/OptionProcessorsStore.ts`

Registers custom option processors:

```typescript
Navigation.addOptionProcessor('topBar.title.text', (value, commandName) => {
  return value.toUpperCase(); // Transform all titles
});
```

### LayoutProcessorsStore
**File**: `processors/LayoutProcessorsStore.ts`

Registers layout transformers:

```typescript
Navigation.addLayoutProcessor((layout, commandName) => {
  // Add default options to all components
  return layout;
});
```

## Interfaces

### Layout Types
**File**: `interfaces/Layout.ts`

```typescript
interface LayoutRoot {
  root: Layout;
  modals?: any[];
  overlays?: any[];
}

type Layout = {
  component?: LayoutComponent;
  stack?: LayoutStack;
  bottomTabs?: LayoutBottomTabs;
  sideMenu?: LayoutSideMenu;
  splitView?: LayoutSplitView;
  topTabs?: LayoutTopTabs;
  externalComponent?: ExternalComponent;
}
```

### Options Interface
**File**: `interfaces/Options.ts` (comprehensive)

Contains all configuration options for:
- Status bar, navigation bars
- Top bar (buttons, title, search)
- Bottom tabs
- Side menus
- Modals, overlays
- Animations
- Layout parameters

### NavigationComponent
**File**: `interfaces/NavigationComponent.ts`

Base class for navigation-aware components:

```typescript
class MyScreen extends NavigationComponent<Props> {
  static options: Options = { ... };

  componentDidAppear(event) { }
  navigationButtonPressed(event) { }
}
```

## Dependencies

**Production**:
- `lodash` - utility functions
- `hoist-non-react-statics` - HOC support
- `react-lifecycles-compat` - lifecycle polyfill
- `tslib` - TypeScript helpers

**Peer**:
- `react`, `react-native` (required)
- `remx` (optional state management)

## Build Output

Uses `react-native-builder-bob`:
- **Source**: `src/`
- **Output**: `lib/`
- **Targets**: ESM modules + TypeScript declarations
