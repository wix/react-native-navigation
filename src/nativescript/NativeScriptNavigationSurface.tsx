import * as React from 'react';
import { Dimensions, StyleSheet, View } from 'react-native';
import { Store } from '../components/Store';
import { Layout } from '../interfaces/Layout';
import { Options } from '../interfaces/Options';

type NativeScriptModule = typeof import('@nativescript/react-native');

type ParsedLayoutNode = {
  id?: string;
  type?: string;
  data?: {
    name?: string | number;
    passProps?: object;
    options?: Options;
  };
  children?: ParsedLayoutNode[];
};

type NativeScriptNavigationSnapshot = {
  root?: ParsedLayoutNode;
  modals: ParsedLayoutNode[];
  overlays: ParsedLayoutNode[];
};

type NavigationSurfaceState = {
  snapshot: NativeScriptNavigationSnapshot;
  defaultOptions: Options;
  mergedOptions: Record<string, Options>;
};

type NavigationStoreListener = () => void;

type NativeScriptNavigationStore = {
  getState(): NavigationSurfaceState;
  subscribe(listener: NavigationStoreListener): () => void;
  setRoot(root: ParsedLayoutNode, modals?: ParsedLayoutNode[], overlays?: ParsedLayoutNode[]): void;
  setDefaultOptions(options: Options): void;
  mergeOptions(componentId: string, options: Options): void;
  push(componentId: string, layout: ParsedLayoutNode): void;
  pop(componentId: string): string | undefined;
  popTo(componentId: string): string | undefined;
  popToRoot(componentId: string): string | undefined;
  setStackRoot(componentId: string, children: ParsedLayoutNode[]): void;
  showModal(layout: ParsedLayoutNode): void;
  dismissModal(componentId: string): string | undefined;
  dismissAllModals(): void;
  showOverlay(layout: ParsedLayoutNode): void;
  dismissOverlay(componentId: string): string | undefined;
  dismissAllOverlays(): void;
};

const NativeScript = require('@nativescript/react-native') as
  | NativeScriptModule
  | (NativeScriptModule & {default?: NativeScriptModule});
const NativeScriptRuntime = ((NativeScript as {default?: NativeScriptModule}).default ??
  NativeScript) as NativeScriptModule;
const initialWindowHeight = Dimensions.get('window').height;

function makeNativeScriptNavigationStore(): NativeScriptNavigationStore {
  let state: NavigationSurfaceState = {
    snapshot: {modals: [], overlays: []},
    defaultOptions: {},
    mergedOptions: {},
  };
  const listeners = new Set<NavigationStoreListener>();

  const emit = () => {
    for (const listener of Array.from(listeners)) {
      listener();
    }
  };

  const replaceState = (next: NavigationSurfaceState) => {
    state = next;
    emit();
  };

  const mutateTree = (
    root: ParsedLayoutNode | undefined,
    componentId: string,
    mutator: (node: ParsedLayoutNode, parent?: ParsedLayoutNode) => void,
    parent?: ParsedLayoutNode,
  ): boolean => {
    if (!root) {
      return false;
    }
    if (root.id === componentId) {
      mutator(root, parent);
      return true;
    }
    for (const child of root.children ?? []) {
      if (mutateTree(child, componentId, mutator, root)) {
        return true;
      }
    }
    return false;
  };

  const removeFromTree = (
    root: ParsedLayoutNode | undefined,
    componentId: string,
  ): string | undefined => {
    if (!root?.children) {
      return undefined;
    }
    const index = root.children.findIndex((child) => child.id === componentId);
    if (index >= 0) {
      return root.children.splice(index, 1)[0]?.id;
    }
    for (const child of root.children) {
      const removed = removeFromTree(child, componentId);
      if (removed) {
        return removed;
      }
    }
    return undefined;
  };

  return {
    getState: () => state,
    subscribe(listener) {
      listeners.add(listener);
      return () => listeners.delete(listener);
    },
    setRoot(root, modals = [], overlays = []) {
      replaceState({
        ...state,
        snapshot: {root, modals, overlays},
      });
    },
    setDefaultOptions(options) {
      replaceState({...state, defaultOptions: options});
    },
    mergeOptions(componentId, options) {
      replaceState({
        ...state,
        mergedOptions: {
          ...state.mergedOptions,
          [componentId]: {
            ...(state.mergedOptions[componentId] ?? {}),
            ...options,
          },
        },
      });
    },
    push(componentId, layout) {
      const snapshot = cloneSnapshot(state.snapshot);
      let didPush = false;
      mutateTree(snapshot.root, componentId, (node, parent) => {
        const stackNode = node.type === 'Stack' ? node : parent?.type === 'Stack' ? parent : undefined;
        if (stackNode) {
          stackNode.children = [...(stackNode.children ?? []), layout];
          didPush = true;
        }
      });
      if (didPush) {
        replaceState({...state, snapshot});
      }
    },
    pop(componentId) {
      const snapshot = cloneSnapshot(state.snapshot);
      let removed: string | undefined;
      mutateTree(snapshot.root, componentId, (_node, parent) => {
        if (parent?.type === 'Stack' && parent.children && parent.children.length > 1) {
          removed = parent.children.pop()?.id;
        }
      });
      if (removed) {
        replaceState({...state, snapshot});
      }
      return removed;
    },
    popTo(componentId) {
      const snapshot = cloneSnapshot(state.snapshot);
      let removed: string | undefined;
      const trim = (node: ParsedLayoutNode): boolean => {
        if (node.type === 'Stack' && node.children?.some((child) => child.id === componentId)) {
          const index = node.children.findIndex((child) => child.id === componentId);
          removed = node.children[node.children.length - 1]?.id;
          node.children = node.children.slice(0, index + 1);
          return true;
        }
        return (node.children ?? []).some(trim);
      };
      if (snapshot.root && trim(snapshot.root)) {
        replaceState({...state, snapshot});
      }
      return removed;
    },
    popToRoot(componentId) {
      const snapshot = cloneSnapshot(state.snapshot);
      let removed: string | undefined;
      mutateTree(snapshot.root, componentId, (_node, parent) => {
        if (parent?.type === 'Stack' && parent.children && parent.children.length > 1) {
          removed = parent.children[parent.children.length - 1]?.id;
          parent.children = parent.children.slice(0, 1);
        }
      });
      if (removed) {
        replaceState({...state, snapshot});
      }
      return removed;
    },
    setStackRoot(componentId, children) {
      const snapshot = cloneSnapshot(state.snapshot);
      mutateTree(snapshot.root, componentId, (node) => {
        if (node.type === 'Stack') {
          node.children = children;
        }
      });
      replaceState({...state, snapshot});
    },
    showModal(layout) {
      replaceState({
        ...state,
        snapshot: {
          ...state.snapshot,
          modals: [...state.snapshot.modals, layout],
        },
      });
    },
    dismissModal(componentId) {
      const snapshot = cloneSnapshot(state.snapshot);
      const index = snapshot.modals.findIndex((modal) => includesLayoutId(modal, componentId));
      if (index < 0) {
        return undefined;
      }
      const removed = snapshot.modals.splice(index, 1)[0]?.id ?? componentId;
      replaceState({...state, snapshot});
      return removed;
    },
    dismissAllModals() {
      replaceState({
        ...state,
        snapshot: {...state.snapshot, modals: []},
      });
    },
    showOverlay(layout) {
      replaceState({
        ...state,
        snapshot: {
          ...state.snapshot,
          overlays: [...state.snapshot.overlays, layout],
        },
      });
    },
    dismissOverlay(componentId) {
      const snapshot = cloneSnapshot(state.snapshot);
      let removed: string | undefined;
      const direct = snapshot.overlays.findIndex((overlay) => overlay.id === componentId);
      if (direct >= 0) {
        removed = snapshot.overlays.splice(direct, 1)[0]?.id;
      } else {
        for (const overlay of snapshot.overlays) {
          removed = removeFromTree(overlay, componentId);
          if (removed) {
            break;
          }
        }
      }
      if (removed) {
        replaceState({...state, snapshot});
      }
      return removed;
    },
    dismissAllOverlays() {
      replaceState({
        ...state,
        snapshot: {...state.snapshot, overlays: []},
      });
    },
  };
}

const navigationStore = makeNativeScriptNavigationStore();

export function getNativeScriptNavigationStore(): NativeScriptNavigationStore {
  return navigationStore;
}

export function normalizeNativeScriptLayout(layout: Layout | ParsedLayoutNode): ParsedLayoutNode {
  if ((layout as ParsedLayoutNode).type) {
    return cloneNode(layout as ParsedLayoutNode);
  }

  const source = layout as Layout;
  if (source.component) {
    return {
      id: source.component.id,
      type: 'Component',
      data: {
        name: source.component.name,
        passProps: source.component.passProps,
        options: source.component.options,
      },
    };
  }
  if (source.externalComponent) {
    return {
      id: source.externalComponent.id,
      type: 'ExternalComponent',
      data: {
        name: source.externalComponent.name,
        passProps: source.externalComponent.passProps,
        options: source.externalComponent.options,
      },
    };
  }
  if (source.stack) {
    return {
      id: source.stack.id,
      type: 'Stack',
      data: {options: source.stack.options},
      children: (source.stack.children ?? []).map((child) =>
        normalizeNativeScriptLayout(child as Layout),
      ),
    };
  }
  if (source.bottomTabs) {
    return {
      id: source.bottomTabs.id,
      type: 'BottomTabs',
      data: {options: source.bottomTabs.options},
      children: (source.bottomTabs.children ?? []).map((child) =>
        normalizeNativeScriptLayout(child as Layout),
      ),
    };
  }
  if (source.topTabs) {
    return {
      id: source.topTabs.id,
      type: 'TopTabs',
      data: {options: source.topTabs.options},
      children: (source.topTabs.children ?? []).map((child) =>
        normalizeNativeScriptLayout(child as Layout),
      ),
    };
  }
  if (source.sideMenu) {
    return normalizeNativeScriptLayout(source.sideMenu.center as Layout);
  }
  if (source.splitView) {
    return normalizeNativeScriptLayout((source.splitView.detail ?? source.splitView.master) as Layout);
  }
  return {type: 'Component', data: {name: 'Unknown'}};
}

export function NativeScriptNavigationSurface({
  attachController = true,
  store,
}: {
  attachController?: boolean;
  store: Store;
}) {
  return (
    <NativeScriptNavigationSurfaceContent
      attachController={attachController}
      store={store}
    />
  );
}

export function NativeScriptNavigationSurfaceContent({
  attachController,
  store,
}: {
  attachController: boolean;
  store: Store;
}) {
  const state = React.useSyncExternalStore(
    navigationStore.subscribe,
    navigationStore.getState,
    navigationStore.getState,
  );

  return (
    <View collapsable={false} style={styles.rootFill}>
      {state.snapshot.root ? (
        <NativeScriptLayoutNode
          node={state.snapshot.root}
          attachRootController={attachController}
          store={store}
          mergedOptions={state.mergedOptions}
        />
      ) : null}
      {state.snapshot.modals.map((modal) => (
        <NativeScriptModalNode
          key={modal.id}
          node={modal}
          attachRootController
          store={store}
          mergedOptions={state.mergedOptions}
        />
      ))}
      {state.snapshot.overlays.map((overlay) => (
        <NativeScriptOverlayNode
          key={overlay.id}
          node={overlay}
          attachRootController
          store={store}
          mergedOptions={state.mergedOptions}
        />
      ))}
    </View>
  );
}

function NativeScriptLayoutNode({
  attachRootController,
  node,
  parentId,
  store,
  mergedOptions,
}: {
  attachRootController: boolean;
  node: ParsedLayoutNode;
  parentId?: string;
  store: Store;
  mergedOptions: Record<string, Options>;
}) {
  const componentId = node.id ?? `${node.type ?? 'node'}-${Math.random().toString(36).slice(2)}`;
  const options = mergeOptions(node.data?.options, mergedOptions[componentId]);
  const attachToReactView = parentId == null;

  if (node.type === 'Stack') {
    return (
      <NativeScriptStackController
        attachController={parentId == null ? attachRootController : false}
        attachNativeView={attachToReactView}
        componentId={componentId}
        options={options}
        parentId={parentId}
        style={styles.rootFill}>
        {(node.children ?? []).map((child) => (
          <NativeScriptLayoutNode
            key={child.id}
            attachRootController={attachRootController}
            node={child}
            parentId={componentId}
            store={store}
            mergedOptions={mergedOptions}
          />
        ))}
      </NativeScriptStackController>
    );
  }

  if (node.type === 'BottomTabs' || node.type === 'TopTabs') {
    return (
      <NativeScriptTabsController
        attachController={parentId == null ? attachRootController : false}
        attachNativeView={attachToReactView}
        componentId={componentId}
        options={options}
        parentId={parentId}
        style={styles.rootFill}>
        {(node.children ?? []).map((child) => (
          <NativeScriptLayoutNode
            key={child.id}
            attachRootController={attachRootController}
            node={child}
            parentId={componentId}
            store={store}
            mergedOptions={mergedOptions}
          />
        ))}
      </NativeScriptTabsController>
    );
  }

  return (
    <NativeScriptComponentController
      attachController={parentId == null ? attachRootController : false}
      attachNativeView={attachToReactView}
      componentId={componentId}
      componentName={node.data?.name}
      options={options}
      parentId={parentId}
      style={styles.rootFill}>
      <RegisteredComponent
        componentId={componentId}
        componentName={node.data?.name}
        passProps={node.data?.passProps}
        store={store}
      />
    </NativeScriptComponentController>
  );
}

function NativeScriptModalNode({
  attachRootController,
  node,
  store,
  mergedOptions,
}: {
  attachRootController: boolean;
  node: ParsedLayoutNode;
  store: Store;
  mergedOptions: Record<string, Options>;
}) {
  return (
    <NativeScriptPresentedController componentId={node.id} presentation="modal" style={styles.fill}>
      <NativeScriptLayoutNode
        attachRootController={attachRootController}
        node={node}
        store={store}
        mergedOptions={mergedOptions}
      />
    </NativeScriptPresentedController>
  );
}

function NativeScriptOverlayNode({
  attachRootController,
  node,
  store,
  mergedOptions,
}: {
  attachRootController: boolean;
  node: ParsedLayoutNode;
  store: Store;
  mergedOptions: Record<string, Options>;
}) {
  return (
    <NativeScriptPresentedController componentId={node.id} presentation="overlay" style={styles.fill}>
      <NativeScriptLayoutNode
        attachRootController={attachRootController}
        node={node}
        store={store}
        mergedOptions={mergedOptions}
      />
    </NativeScriptPresentedController>
  );
}

function RegisteredComponent({
  componentId,
  componentName,
  passProps,
  store,
}: {
  componentId: string;
  componentName?: string | number;
  passProps?: object;
  store: Store;
}) {
  if (componentName == null) {
    return null;
  }
  const componentProvider = store.getComponentClassForName(componentName);
  if (!componentProvider) {
    return null;
  }
  const Component = componentProvider() as React.ComponentType<any>;
  return (
    <View collapsable={false} style={styles.rootFill}>
      <Component {...passProps} componentId={componentId} />
    </View>
  );
}

function viewContainsDescendant(view: any, descendant: any): boolean {
  'worklet';
  if (!view || !descendant || typeof descendant.isDescendantOfView !== 'function') {
    return false;
  }
  try {
    return descendant.isDescendantOfView(view) === true;
  } catch {
    return false;
  }
}

function isNativeKind(view: any, nativeClass: any): boolean {
  'worklet';
  if (!view || !nativeClass || typeof view.isKindOfClass !== 'function') {
    return false;
  }
  try {
    return view.isKindOfClass(nativeClass) === true;
  } catch {
    return false;
  }
}

function firstDescendantOfKind(view: any, nativeClass: any, depth: number): any {
  'worklet';
  if (!view || depth > 6) {
    return null;
  }
  if (isNativeKind(view, nativeClass)) {
    return view;
  }
  const subviews = view.subviews;
  const count = !subviews ? 0 : typeof subviews.count === 'number' ? subviews.count : (subviews.length ?? 0);
  for (let index = 0; index < count; index++) {
    const subview =
      subviews && typeof subviews.objectAtIndex === 'function'
        ? subviews.objectAtIndex(index)
        : subviews?.[index];
    const found = firstDescendantOfKind(subview, nativeClass, depth + 1);
    if (found) {
      return found;
    }
  }
  return null;
}

function keepAncestorTabBarFront(controller: any) {
  'worklet';
  const startView = controller?.view;
  const globalObject = globalThis as Record<string, any>;
  const api = globalObject.__nativeScriptNativeApi;
  const UITabBar = api?.UITabBar ?? globalObject.UITabBar;
  if (!startView || !UITabBar) {
    return;
  }
  let currentView = startView;
  for (let depth = 0; currentView && depth < 12; depth++) {
    const parent = currentView.superview;
    if (!parent) {
      return;
    }
    const subviews = parent.subviews;
    const count = !subviews ? 0 : typeof subviews.count === 'number' ? subviews.count : (subviews.length ?? 0);
    for (let index = 0; index < count; index++) {
      const candidate =
        subviews && typeof subviews.objectAtIndex === 'function'
          ? subviews.objectAtIndex(index)
          : subviews?.[index];
      if (!candidate || candidate === currentView || viewContainsDescendant(candidate, startView)) {
        continue;
      }
      const tabBar = firstDescendantOfKind(candidate, UITabBar, 0);
      if (!tabBar) {
        continue;
      }
      tabBar.hidden = false;
      tabBar.alpha = 1;
      tabBar.userInteractionEnabled = true;
      if (tabBar.layer) {
        tabBar.layer.zPosition = 1000;
      }
      if (candidate.layer) {
        candidate.layer.zPosition = 1000;
      }
      if (typeof parent.bringSubviewToFront === 'function') {
        parent.bringSubviewToFront(candidate);
      }
      return;
    }
    currentView = parent;
  }
}

function scheduleAncestorTabBarFront(controller: any) {
  'worklet';
  keepAncestorTabBarFront(controller);
  if (typeof setTimeout !== 'function') {
    return;
  }
  const restore = () => {
    'worklet';
    keepAncestorTabBarFront(controller);
  };
  setTimeout(restore, 0);
  setTimeout(restore, 32);
  setTimeout(restore, 120);
}

function nativeValue(name: string): any {
  'worklet';
  const globalObject = globalThis as Record<string, any>;
  const api = globalObject.__nativeScriptNativeApi;
  return api?.[name] ?? globalObject[name];
}

function nativeColor(value: unknown, fallback: string): any {
  'worklet';
  const UIColor = nativeValue('UIColor');
  if (!UIColor) {
    return null;
  }
  if (typeof value === 'string' && value[0] === '#' && value.length === 7) {
    const red = parseInt(value.slice(1, 3), 16) / 255;
    const green = parseInt(value.slice(3, 5), 16) / 255;
    const blue = parseInt(value.slice(5, 7), 16) / 255;
    if (
      Number.isFinite(red) &&
      Number.isFinite(green) &&
      Number.isFinite(blue) &&
      typeof UIColor.colorWithRedGreenBlueAlpha === 'function'
    ) {
      return UIColor.colorWithRedGreenBlueAlpha(red, green, blue, 1);
    }
  }
  if (value && typeof value === 'object') {
    return value;
  }
  return UIColor[fallback] ?? null;
}

function arrayItem(array: any, index: number): any {
  'worklet';
  if (!array) {
    return null;
  }
  if (typeof array.objectAtIndex === 'function') {
    return array.objectAtIndex(index);
  }
  return array[index];
}

function arrayCount(array: any): number {
  'worklet';
  if (!array) {
    return 0;
  }
  return typeof array.count === 'number' ? array.count : (array.length ?? 0);
}

function flexibleSizeMask(): number {
  'worklet';
  const autoresizing = nativeValue('UIViewAutoresizing');
  return (autoresizing?.FlexibleWidth ?? 2) + (autoresizing?.FlexibleHeight ?? 16);
}

function isNativeScrollView(view: any): boolean {
  'worklet';
  const UIScrollView = nativeValue('UIScrollView');
  if (!view || !UIScrollView || typeof view.isKindOfClass !== 'function') {
    return false;
  }
  try {
    return view.isKindOfClass(UIScrollView) === true;
  } catch {
    return false;
  }
}

function shouldFillHostedSubview(rootView: any, subview: any): boolean {
  'worklet';
  const parentBounds = rootView?.bounds ?? rootView?.frame;
  const frame = subview?.frame;
  const parentWidth = parentBounds?.size?.width ?? 0;
  const childWidth = frame?.size?.width ?? 0;
  const originX = frame?.origin?.x ?? 0;
  const originY = frame?.origin?.y ?? 0;
  if (parentWidth <= 0) {
    return false;
  }
  return (
    Math.abs(originX) < 1 &&
    Math.abs(originY) < 1 &&
    (childWidth <= 0 || Math.abs(childWidth - parentWidth) < 2)
  );
}

function layoutSingleHostedSubviewChain(rootView: any, depth: number) {
  'worklet';
  if (!rootView || depth > 8 || isNativeScrollView(rootView)) {
    return;
  }
  const subviews = rootView.subviews;
  const count = arrayCount(subviews);
  for (let index = 0; index < count; index++) {
    const subview = arrayItem(subviews, index);
    if (!subview || !shouldFillHostedSubview(rootView, subview)) {
      continue;
    }
    subview.frame = rootView.bounds;
    subview.autoresizingMask = flexibleSizeMask();
    if (typeof subview.setNeedsLayout === 'function') {
      subview.setNeedsLayout();
    }
    if (typeof subview.layoutIfNeeded === 'function') {
      subview.layoutIfNeeded();
    }
    layoutSingleHostedSubviewChain(subview, depth + 1);
  }
}

function layoutHostedReactSubviews(controller: any) {
  'worklet';
  const rootView = controller?.view;
  if (!rootView) {
    return;
  }
  if (typeof rootView.setNeedsLayout === 'function') {
    rootView.setNeedsLayout();
  }
  if (typeof rootView.layoutIfNeeded === 'function') {
    rootView.layoutIfNeeded();
  }
  const mask = flexibleSizeMask();
  const subviews = rootView.subviews;
  const count = arrayCount(subviews);
  for (let index = 0; index < count; index++) {
    const subview = arrayItem(subviews, index);
    if (subview) {
      subview.frame = rootView.bounds;
      subview.autoresizingMask = mask;
      layoutSingleHostedSubviewChain(subview, 0);
    }
  }
}

function layoutVisibleController(controller: any) {
  'worklet';
  if (!controller?.view) {
    return;
  }
  if (typeof controller.view.setNeedsLayout === 'function') {
    controller.view.setNeedsLayout();
  }
  if (typeof controller.view.layoutIfNeeded === 'function') {
    controller.view.layoutIfNeeded();
  }
  const visibleController =
    controller.visibleViewController ?? controller.selectedViewController ?? controller.topViewController;
  if (visibleController && visibleController !== controller) {
    layoutVisibleController(visibleController);
    return;
  }
  layoutHostedReactSubviews(controller);
}

function rectEdgeAll(): number {
  'worklet';
  const edge = nativeValue('UIRectEdge');
  return edge?.All ?? edge?.all ?? 15;
}

function configureExtendedLayout(controller: any) {
  'worklet';
  if (!controller) {
    return;
  }
  controller.edgesForExtendedLayout = rectEdgeAll();
  controller.extendedLayoutIncludesOpaqueBars = true;
}

function configureNavigationBar(controller: any) {
  'worklet';
  const navigationBar = controller?.navigationBar;
  if (!navigationBar) {
    return;
  }
  configureExtendedLayout(controller);
  navigationBar.translucent = true;
  navigationBar.prefersLargeTitles = false;
  const UIColor = nativeValue('UIColor');
  const clearColor = UIColor?.clearColor ?? null;
  if (clearColor) {
    navigationBar.backgroundColor = clearColor;
  }
  const UINavigationBarAppearance = nativeValue('UINavigationBarAppearance');
  if (UINavigationBarAppearance && typeof UINavigationBarAppearance.alloc === 'function') {
    const allocated = UINavigationBarAppearance.alloc();
    const appearance =
      allocated && typeof allocated.init === 'function' ? allocated.init() : allocated;
    if (typeof appearance.configureWithTransparentBackground === 'function') {
      appearance.configureWithTransparentBackground();
    } else if (typeof appearance.configureWithDefaultBackground === 'function') {
      appearance.configureWithDefaultBackground();
    }
    if (clearColor) {
      appearance.backgroundColor = clearColor;
      appearance.shadowColor = clearColor;
    }
    navigationBar.standardAppearance = appearance;
    navigationBar.scrollEdgeAppearance = appearance;
    navigationBar.compactAppearance = appearance;
  }
}

const NativeScriptComponentController = NativeScriptRuntime.defineUIViewController<{
  componentId: string;
  componentName?: string | number;
  options?: Options;
  parentId?: string;
  style?: unknown;
}, any>({
  debugName: 'RNNComponentViewController',
  layout: {sizing: 'fill'},
  createController({componentId, componentName, options}) {
    'worklet';
    const api = (globalThis as Record<string, any>).__nativeScriptNativeApi;
    const globals = globalThis as Record<string, any>;
    const UIViewController = api?.UIViewController ?? globals.UIViewController;
    if (!UIViewController || typeof UIViewController.alloc !== 'function') {
      throw new Error(`UIViewController is not allocatable in the UI runtime: ${typeof UIViewController}`);
    }
    const allocated = UIViewController.alloc();
    const controller = allocated && typeof allocated.init === 'function' ? allocated.init() : allocated;
    configureExtendedLayout(controller);
    const UIColor = api?.UIColor ?? globals.UIColor;
    controller.view.backgroundColor =
      nativeColor('#f6f7fb', 'systemBackgroundColor') ?? UIColor.systemBackgroundColor;
    const topBarTitle = (options as any)?.topBar?.title?.text;
    const bottomTabTitle = (options as any)?.bottomTab?.text;
    controller.title = topBarTitle ?? bottomTabTitle ?? (componentName != null ? '' + componentName : componentId);
    const UITabBarItem = api?.UITabBarItem ?? globals.UITabBarItem;
    if (UITabBarItem && typeof UITabBarItem.alloc === 'function') {
      const itemAllocated = UITabBarItem.alloc();
      const tabBarItem = itemAllocated && typeof itemAllocated.init === 'function' ? itemAllocated.init() : itemAllocated;
      tabBarItem.title = bottomTabTitle ?? controller.title;
      const badge = (options as any)?.bottomTab?.badge;
      if (badge != null) {
        tabBarItem.badgeValue = '' + badge;
      }
      const testID = (options as any)?.bottomTab?.testID;
      if (testID != null) {
        tabBarItem.accessibilityIdentifier = '' + testID;
      }
      controller.tabBarItem = tabBarItem;
    }
    return controller;
  },
  childrenView(controller) {
    'worklet';
    return controller.view;
  },
  update(controller, props) {
    'worklet';
    const topBarTitle = (props.options as any)?.topBar?.title?.text;
    const bottomTabTitle = (props.options as any)?.bottomTab?.text;
    controller.title =
      topBarTitle ?? bottomTabTitle ?? (props.componentName != null ? '' + props.componentName : props.componentId);
    const api = (globalThis as Record<string, any>).__nativeScriptNativeApi;
    const globals = globalThis as Record<string, any>;
    const UITabBarItem = api?.UITabBarItem ?? globals.UITabBarItem;
    if (UITabBarItem && typeof UITabBarItem.alloc === 'function') {
      const itemAllocated = UITabBarItem.alloc();
      const tabBarItem = itemAllocated && typeof itemAllocated.init === 'function' ? itemAllocated.init() : itemAllocated;
      tabBarItem.title = bottomTabTitle ?? controller.title;
      const badge = (props.options as any)?.bottomTab?.badge;
      if (badge != null) {
        tabBarItem.badgeValue = '' + badge;
      }
      const testID = (props.options as any)?.bottomTab?.testID;
      if (testID != null) {
        tabBarItem.accessibilityIdentifier = '' + testID;
      }
      controller.tabBarItem = tabBarItem;
    }
    const key = '__rnnNativeScriptNavigationRegistry';
    const globalObject = globalThis as Record<string, any>;
    const registry = globalObject[key] ?? {controllers: {}, kinds: {}, parentChildren: {}};
    globalObject[key] = registry;
    registry.controllers[props.componentId] = controller;
    registry.kinds[props.componentId] = 'component';
    if (props.parentId) {
      const children = registry.parentChildren[props.parentId] ?? [];
      let exists = false;
      for (let index = 0; index < children.length; index++) {
        if (children[index] === props.componentId) {
          exists = true;
          break;
        }
      }
      if (!exists) {
        children[children.length] = props.componentId;
        registry.parentChildren[props.parentId] = children;
      }
      const parent = registry.controllers[props.parentId];
      if (parent) {
        const childControllers: any[] = [];
        for (let index = 0; index < children.length; index++) {
          const child = registry.controllers[children[index]];
          if (child) {
            const childParent = child.parentViewController;
            if (childParent && childParent !== parent) {
              child.willMoveToParentViewController(null);
              child.removeFromParentViewController();
            }
            if (child.view?.superview) {
              child.view.removeFromSuperview();
            }
            childControllers[childControllers.length] = child;
          }
        }
        const kind = registry.kinds[props.parentId];
        const NSArray = api?.NSArray ?? globals.NSArray;
        const nativeChildControllers =
          NSArray && typeof NSArray.arrayWithArray === 'function'
            ? NSArray.arrayWithArray(childControllers)
            : childControllers;
        if (kind === 'stack' && childControllers.length > 0) {
          parent.setViewControllersAnimated(nativeChildControllers, false);
        } else if (kind === 'tabs') {
          if (typeof parent.setViewControllersAnimated === 'function') {
            parent.setViewControllersAnimated(nativeChildControllers, false);
          } else {
            parent.viewControllers = nativeChildControllers;
          }
          if (parent.selectedIndex >= childControllers.length) {
            parent.selectedIndex = 0;
          }
        }
        layoutVisibleController(parent);
      }
    }
    layoutVisibleController(controller);
    scheduleAncestorTabBarFront(controller);
  },
  mounted(controller, props) {
    'worklet';
    const key = '__rnnNativeScriptNavigationRegistry';
    const globalObject = globalThis as Record<string, any>;
    const registry = globalObject[key] ?? {controllers: {}, kinds: {}, parentChildren: {}};
    globalObject[key] = registry;
    registry.controllers[props.componentId] = controller;
    registry.kinds[props.componentId] = 'component';
    if (props.parentId) {
      const children = registry.parentChildren[props.parentId] ?? [];
      let exists = false;
      for (let index = 0; index < children.length; index++) {
        if (children[index] === props.componentId) {
          exists = true;
          break;
        }
      }
      if (!exists) {
        children[children.length] = props.componentId;
        registry.parentChildren[props.parentId] = children;
      }
      const parent = registry.controllers[props.parentId];
      if (parent) {
        const childControllers: any[] = [];
        for (let index = 0; index < children.length; index++) {
          const child = registry.controllers[children[index]];
          if (child) {
            const childParent = child.parentViewController;
            if (childParent && childParent !== parent) {
              child.willMoveToParentViewController(null);
              child.removeFromParentViewController();
            }
            if (child.view?.superview) {
              child.view.removeFromSuperview();
            }
            childControllers[childControllers.length] = child;
          }
        }
        const kind = registry.kinds[props.parentId];
        const api = globalObject.__nativeScriptNativeApi;
        const NSArray = api?.NSArray ?? globalObject.NSArray;
        const nativeChildControllers =
          NSArray && typeof NSArray.arrayWithArray === 'function'
            ? NSArray.arrayWithArray(childControllers)
            : childControllers;
        if (kind === 'stack' && childControllers.length > 0) {
          parent.setViewControllersAnimated(nativeChildControllers, false);
        } else if (kind === 'tabs') {
          if (typeof parent.setViewControllersAnimated === 'function') {
            parent.setViewControllersAnimated(nativeChildControllers, false);
          } else {
            parent.viewControllers = nativeChildControllers;
          }
          if (parent.selectedIndex >= childControllers.length) {
            parent.selectedIndex = 0;
          }
        }
        layoutVisibleController(parent);
      }
    }
    layoutVisibleController(controller);
    scheduleAncestorTabBarFront(controller);
  },
  dispose(_controller, props) {
    'worklet';
    const key = '__rnnNativeScriptNavigationRegistry';
    const globalObject = globalThis as Record<string, any>;
    const registry = globalObject[key];
    if (!registry) {
      return;
    }
    registry.controllers[props.componentId] = undefined;
    registry.kinds[props.componentId] = undefined;
    registry.parentChildren[props.componentId] = undefined;
    if (props.parentId) {
      const children = registry.parentChildren[props.parentId] ?? [];
      for (let index = children.length - 1; index >= 0; index--) {
        if (children[index] === props.componentId) {
          for (let moveIndex = index; moveIndex < children.length - 1; moveIndex++) {
            children[moveIndex] = children[moveIndex + 1];
          }
          children.length = children.length - 1;
        }
      }
      registry.parentChildren[props.parentId] = children;
    }
  },
});

const NativeScriptStackController = NativeScriptRuntime.defineUIViewController<{
  componentId: string;
  options?: Options;
  parentId?: string;
  style?: unknown;
  }, any>({
  debugName: 'RNNStackController',
  layout: {sizing: 'fill'},
  createController() {
    'worklet';
    const api = (globalThis as Record<string, any>).__nativeScriptNativeApi;
    const globals = globalThis as Record<string, any>;
    const UIViewController = api?.UIViewController ?? globals.UIViewController;
    const UINavigationController = api?.UINavigationController ?? globals.UINavigationController;
    if (!UIViewController || typeof UIViewController.alloc !== 'function') {
      throw new Error(`UIViewController is not allocatable in the UI runtime: ${typeof UIViewController}`);
    }
    if (!UINavigationController || typeof UINavigationController.alloc !== 'function') {
      throw new Error(`UINavigationController is not allocatable in the UI runtime: ${typeof UINavigationController}`);
    }
    const placeholderAllocated = UIViewController.alloc();
    const placeholder =
      placeholderAllocated && typeof placeholderAllocated.init === 'function'
        ? placeholderAllocated.init()
        : placeholderAllocated;
    const UIColor = api?.UIColor ?? globals.UIColor;
    placeholder.view.backgroundColor =
      nativeColor('#f6f7fb', 'systemBackgroundColor') ?? UIColor.systemBackgroundColor;
    const allocated = UINavigationController.alloc();
    const controller = allocated && typeof allocated.init === 'function' ? allocated.init() : allocated;
    configureExtendedLayout(controller);
    configureNavigationBar(controller);
    const UIView = api?.UIView ?? globals.UIView;
    if (UIView && typeof UIView.alloc === 'function') {
      const mountViewAllocated = UIView.alloc();
      const mountView =
        mountViewAllocated && typeof mountViewAllocated.init === 'function'
          ? mountViewAllocated.init()
          : mountViewAllocated;
      mountView.tag = 83922041;
      mountView.hidden = true;
      mountView.userInteractionEnabled = false;
      controller.view.addSubview(mountView);
    }
    const NSArray = api?.NSArray ?? globals.NSArray;
    const placeholderControllers = [placeholder];
    controller.viewControllers =
      NSArray && typeof NSArray.arrayWithArray === 'function'
        ? NSArray.arrayWithArray(placeholderControllers)
        : placeholderControllers;
    return controller;
  },
  childrenView(controller) {
    'worklet';
    const existingMountView =
      controller.view && typeof controller.view.viewWithTag === 'function'
        ? controller.view.viewWithTag(83922041)
        : null;
    if (existingMountView) {
      return existingMountView;
    }
    const api = (globalThis as Record<string, any>).__nativeScriptNativeApi;
    const globals = globalThis as Record<string, any>;
    const UIView = api?.UIView ?? globals.UIView;
    if (UIView && typeof UIView.alloc === 'function') {
      const mountViewAllocated = UIView.alloc();
      const mountView =
        mountViewAllocated && typeof mountViewAllocated.init === 'function'
          ? mountViewAllocated.init()
          : mountViewAllocated;
      mountView.tag = 83922041;
      mountView.hidden = true;
      mountView.userInteractionEnabled = false;
      controller.view.addSubview(mountView);
      return mountView;
    }
    return controller.view;
  },
  update(controller, props) {
    'worklet';
    const topBar = (props.options as any)?.topBar;
    if (topBar?.visible === false) {
      controller.setNavigationBarHiddenAnimated(true, false);
    } else if (topBar?.visible === true) {
      controller.setNavigationBarHiddenAnimated(false, false);
    }
    configureNavigationBar(controller);
    const key = '__rnnNativeScriptNavigationRegistry';
    const globalObject = globalThis as Record<string, any>;
    const registry = globalObject[key] ?? {controllers: {}, kinds: {}, parentChildren: {}};
    globalObject[key] = registry;
    registry.controllers[props.componentId] = controller;
    registry.kinds[props.componentId] = 'stack';
    if (props.parentId) {
      const children = registry.parentChildren[props.parentId] ?? [];
      let exists = false;
      for (let index = 0; index < children.length; index++) {
        if (children[index] === props.componentId) {
          exists = true;
          break;
        }
      }
      if (!exists) {
        children[children.length] = props.componentId;
        registry.parentChildren[props.parentId] = children;
      }
    }
    const ownChildren = registry.parentChildren[props.componentId] ?? [];
    const childControllers: any[] = [];
    for (let index = 0; index < ownChildren.length; index++) {
      const child = registry.controllers[ownChildren[index]];
      if (child) {
        const childParent = child.parentViewController;
        if (childParent && childParent !== controller) {
          child.willMoveToParentViewController(null);
          child.removeFromParentViewController();
        }
        if (child.view?.superview) {
          child.view.removeFromSuperview();
        }
        childControllers[childControllers.length] = child;
      }
    }
    if (childControllers.length > 0) {
      const api = globalObject.__nativeScriptNativeApi;
      const NSArray = api?.NSArray ?? globalObject.NSArray;
      controller.setViewControllersAnimated(
        NSArray && typeof NSArray.arrayWithArray === 'function'
          ? NSArray.arrayWithArray(childControllers)
          : childControllers,
        false,
      );
    }
    layoutVisibleController(controller);
    scheduleAncestorTabBarFront(controller);
  },
  mounted(controller, props) {
    'worklet';
    const key = '__rnnNativeScriptNavigationRegistry';
    const globalObject = globalThis as Record<string, any>;
    const registry = globalObject[key] ?? {controllers: {}, kinds: {}, parentChildren: {}};
    globalObject[key] = registry;
    registry.controllers[props.componentId] = controller;
    registry.kinds[props.componentId] = 'stack';
    if (props.parentId) {
      const children = registry.parentChildren[props.parentId] ?? [];
      let exists = false;
      for (let index = 0; index < children.length; index++) {
        if (children[index] === props.componentId) {
          exists = true;
          break;
        }
      }
      if (!exists) {
        children[children.length] = props.componentId;
        registry.parentChildren[props.parentId] = children;
      }
    }
    const ownChildren = registry.parentChildren[props.componentId] ?? [];
    const childControllers: any[] = [];
    for (let index = 0; index < ownChildren.length; index++) {
      const child = registry.controllers[ownChildren[index]];
      if (child) {
        const childParent = child.parentViewController;
        if (childParent && childParent !== controller) {
          child.willMoveToParentViewController(null);
          child.removeFromParentViewController();
        }
        if (child.view?.superview) {
          child.view.removeFromSuperview();
        }
        childControllers[childControllers.length] = child;
      }
    }
    if (childControllers.length > 0) {
      const api = globalObject.__nativeScriptNativeApi;
      const NSArray = api?.NSArray ?? globalObject.NSArray;
      controller.setViewControllersAnimated(
        NSArray && typeof NSArray.arrayWithArray === 'function'
          ? NSArray.arrayWithArray(childControllers)
          : childControllers,
        false,
      );
    }
    layoutVisibleController(controller);
    scheduleAncestorTabBarFront(controller);
  },
  dispose(_controller, props) {
    'worklet';
    const key = '__rnnNativeScriptNavigationRegistry';
    const globalObject = globalThis as Record<string, any>;
    const registry = globalObject[key];
    if (!registry) {
      return;
    }
    registry.controllers[props.componentId] = undefined;
    registry.kinds[props.componentId] = undefined;
    registry.parentChildren[props.componentId] = undefined;
    if (props.parentId) {
      const children = registry.parentChildren[props.parentId] ?? [];
      for (let index = children.length - 1; index >= 0; index--) {
        if (children[index] === props.componentId) {
          for (let moveIndex = index; moveIndex < children.length - 1; moveIndex++) {
            children[moveIndex] = children[moveIndex + 1];
          }
          children.length = children.length - 1;
        }
      }
      registry.parentChildren[props.parentId] = children;
    }
  },
});

const NativeScriptTabsController = NativeScriptRuntime.defineUIViewController<{
  componentId: string;
  options?: Options;
  parentId?: string;
  style?: unknown;
}, any>({
  debugName: 'RNNBottomTabsController',
  layout: {sizing: 'fill'},
  createController({options}) {
    'worklet';
    const api = (globalThis as Record<string, any>).__nativeScriptNativeApi;
    const globals = globalThis as Record<string, any>;
    const UITabBarController = api?.UITabBarController ?? globals.UITabBarController;
    if (!UITabBarController || typeof UITabBarController.alloc !== 'function') {
      throw new Error(`UITabBarController is not allocatable in the UI runtime: ${typeof UITabBarController}`);
    }
    const allocated = UITabBarController.alloc();
    const controller = allocated && typeof allocated.init === 'function' ? allocated.init() : allocated;
    configureExtendedLayout(controller);
    controller.tabBar.translucent = true;
    const UIView = api?.UIView ?? globals.UIView;
    if (UIView && typeof UIView.alloc === 'function') {
      const mountViewAllocated = UIView.alloc();
      const mountView =
        mountViewAllocated && typeof mountViewAllocated.init === 'function'
          ? mountViewAllocated.init()
          : mountViewAllocated;
      mountView.tag = 83922041;
      mountView.hidden = true;
      mountView.userInteractionEnabled = false;
      controller.view.addSubview(mountView);
    }
    const bottomTabs = (options as any)?.bottomTabs;
    if (typeof bottomTabs?.currentTabIndex === 'number') {
      controller.selectedIndex = bottomTabs.currentTabIndex;
    }
    if (bottomTabs?.visible === false) {
      controller.tabBar.hidden = true;
    } else if (bottomTabs?.visible === true) {
      controller.tabBar.hidden = false;
    }
    return controller;
  },
  childrenView(controller) {
    'worklet';
    const existingMountView =
      controller.view && typeof controller.view.viewWithTag === 'function'
        ? controller.view.viewWithTag(83922041)
        : null;
    if (existingMountView) {
      return existingMountView;
    }
    const api = (globalThis as Record<string, any>).__nativeScriptNativeApi;
    const globals = globalThis as Record<string, any>;
    const UIView = api?.UIView ?? globals.UIView;
    if (UIView && typeof UIView.alloc === 'function') {
      const mountViewAllocated = UIView.alloc();
      const mountView =
        mountViewAllocated && typeof mountViewAllocated.init === 'function'
          ? mountViewAllocated.init()
          : mountViewAllocated;
      mountView.tag = 83922041;
      mountView.hidden = true;
      mountView.userInteractionEnabled = false;
      controller.view.addSubview(mountView);
      return mountView;
    }
    return controller.view;
  },
  update(controller, props) {
    'worklet';
    const bottomTabs = (props.options as any)?.bottomTabs;
    if (typeof bottomTabs?.currentTabIndex === 'number') {
      controller.selectedIndex = bottomTabs.currentTabIndex;
    }
    if (bottomTabs?.visible === false) {
      controller.tabBar.hidden = true;
    } else if (bottomTabs?.visible === true) {
      controller.tabBar.hidden = false;
    }
    const key = '__rnnNativeScriptNavigationRegistry';
    const globalObject = globalThis as Record<string, any>;
    const registry = globalObject[key] ?? {controllers: {}, kinds: {}, parentChildren: {}};
    globalObject[key] = registry;
    registry.controllers[props.componentId] = controller;
    registry.kinds[props.componentId] = 'tabs';
    if (props.parentId) {
      const children = registry.parentChildren[props.parentId] ?? [];
      let exists = false;
      for (let index = 0; index < children.length; index++) {
        if (children[index] === props.componentId) {
          exists = true;
          break;
        }
      }
      if (!exists) {
        children[children.length] = props.componentId;
        registry.parentChildren[props.parentId] = children;
      }
      const parent = registry.controllers[props.parentId];
      if (parent) {
        const childControllers: any[] = [];
        for (let index = 0; index < children.length; index++) {
          const child = registry.controllers[children[index]];
          if (child) {
            const childParent = child.parentViewController;
            if (childParent && childParent !== parent) {
              child.willMoveToParentViewController(null);
              child.removeFromParentViewController();
            }
            if (child.view?.superview) {
              child.view.removeFromSuperview();
            }
            childControllers[childControllers.length] = child;
          }
        }
        const parentKind = registry.kinds[props.parentId];
        const api = globalObject.__nativeScriptNativeApi;
        const NSArray = api?.NSArray ?? globalObject.NSArray;
        const nativeChildControllers =
          NSArray && typeof NSArray.arrayWithArray === 'function'
            ? NSArray.arrayWithArray(childControllers)
            : childControllers;
        if (parentKind === 'stack' && childControllers.length > 0) {
          parent.setViewControllersAnimated(nativeChildControllers, false);
        } else if (parentKind === 'tabs') {
          if (typeof parent.setViewControllersAnimated === 'function') {
            parent.setViewControllersAnimated(nativeChildControllers, false);
          } else {
            parent.viewControllers = nativeChildControllers;
          }
          if (parent.selectedIndex >= childControllers.length) {
            parent.selectedIndex = 0;
          }
        }
        layoutVisibleController(parent);
      }
    }
    const ownChildren = registry.parentChildren[props.componentId] ?? [];
    const childControllers: any[] = [];
    for (let index = 0; index < ownChildren.length; index++) {
      const child = registry.controllers[ownChildren[index]];
      if (child) {
        const childParent = child.parentViewController;
        if (childParent && childParent !== controller) {
          child.willMoveToParentViewController(null);
          child.removeFromParentViewController();
        }
        if (child.view?.superview) {
          child.view.removeFromSuperview();
        }
        childControllers[childControllers.length] = child;
      }
    }
    const api = globalObject.__nativeScriptNativeApi;
    const NSArray = api?.NSArray ?? globalObject.NSArray;
    const nativeChildControllers =
      NSArray && typeof NSArray.arrayWithArray === 'function'
        ? NSArray.arrayWithArray(childControllers)
        : childControllers;
    if (typeof controller.setViewControllersAnimated === 'function') {
      controller.setViewControllersAnimated(nativeChildControllers, false);
    } else {
      controller.viewControllers = nativeChildControllers;
    }
    if (controller.selectedIndex >= childControllers.length) {
      controller.selectedIndex = 0;
    }
    layoutVisibleController(controller);
  },
  mounted(controller, props) {
    'worklet';
    const key = '__rnnNativeScriptNavigationRegistry';
    const globalObject = globalThis as Record<string, any>;
    const registry = globalObject[key] ?? {controllers: {}, kinds: {}, parentChildren: {}};
    globalObject[key] = registry;
    registry.controllers[props.componentId] = controller;
    registry.kinds[props.componentId] = 'tabs';
    if (props.parentId) {
      const children = registry.parentChildren[props.parentId] ?? [];
      let exists = false;
      for (let index = 0; index < children.length; index++) {
        if (children[index] === props.componentId) {
          exists = true;
          break;
        }
      }
      if (!exists) {
        children[children.length] = props.componentId;
        registry.parentChildren[props.parentId] = children;
      }
      const parent = registry.controllers[props.parentId];
      if (parent) {
        const childControllers: any[] = [];
        for (let index = 0; index < children.length; index++) {
          const child = registry.controllers[children[index]];
          if (child) {
            const childParent = child.parentViewController;
            if (childParent && childParent !== parent) {
              child.willMoveToParentViewController(null);
              child.removeFromParentViewController();
            }
            if (child.view?.superview) {
              child.view.removeFromSuperview();
            }
            childControllers[childControllers.length] = child;
          }
        }
        const parentKind = registry.kinds[props.parentId];
        const api = globalObject.__nativeScriptNativeApi;
        const NSArray = api?.NSArray ?? globalObject.NSArray;
        const nativeChildControllers =
          NSArray && typeof NSArray.arrayWithArray === 'function'
            ? NSArray.arrayWithArray(childControllers)
            : childControllers;
        if (parentKind === 'stack' && childControllers.length > 0) {
          parent.setViewControllersAnimated(nativeChildControllers, false);
        } else if (parentKind === 'tabs') {
          if (typeof parent.setViewControllersAnimated === 'function') {
            parent.setViewControllersAnimated(nativeChildControllers, false);
          } else {
            parent.viewControllers = nativeChildControllers;
          }
          if (parent.selectedIndex >= childControllers.length) {
            parent.selectedIndex = 0;
          }
        }
        layoutVisibleController(parent);
      }
    }
    const ownChildren = registry.parentChildren[props.componentId] ?? [];
    const childControllers: any[] = [];
    for (let index = 0; index < ownChildren.length; index++) {
      const child = registry.controllers[ownChildren[index]];
      if (child) {
        const childParent = child.parentViewController;
        if (childParent && childParent !== controller) {
          child.willMoveToParentViewController(null);
          child.removeFromParentViewController();
        }
        if (child.view?.superview) {
          child.view.removeFromSuperview();
        }
        childControllers[childControllers.length] = child;
      }
    }
    const api = globalObject.__nativeScriptNativeApi;
    const NSArray = api?.NSArray ?? globalObject.NSArray;
    const nativeChildControllers =
      NSArray && typeof NSArray.arrayWithArray === 'function'
        ? NSArray.arrayWithArray(childControllers)
        : childControllers;
    if (typeof controller.setViewControllersAnimated === 'function') {
      controller.setViewControllersAnimated(nativeChildControllers, false);
    } else {
      controller.viewControllers = nativeChildControllers;
    }
    if (controller.selectedIndex >= childControllers.length) {
      controller.selectedIndex = 0;
    }
    layoutVisibleController(controller);
  },
  dispose(_controller, props) {
    'worklet';
    const key = '__rnnNativeScriptNavigationRegistry';
    const globalObject = globalThis as Record<string, any>;
    const registry = globalObject[key];
    if (!registry) {
      return;
    }
    registry.controllers[props.componentId] = undefined;
    registry.kinds[props.componentId] = undefined;
    registry.parentChildren[props.componentId] = undefined;
    if (props.parentId) {
      const children = registry.parentChildren[props.parentId] ?? [];
      for (let index = children.length - 1; index >= 0; index--) {
        if (children[index] === props.componentId) {
          for (let moveIndex = index; moveIndex < children.length - 1; moveIndex++) {
            children[moveIndex] = children[moveIndex + 1];
          }
          children.length = children.length - 1;
        }
      }
      registry.parentChildren[props.parentId] = children;
    }
  },
});

const NativeScriptPresentedController = NativeScriptRuntime.defineUIViewController<{
  componentId?: string;
  presentation: 'modal' | 'overlay';
  style?: unknown;
}, any>({
  debugName: 'RNNPresentedController',
  layout: {sizing: 'fill'},
  createController() {
    'worklet';
    const api = (globalThis as Record<string, any>).__nativeScriptNativeApi;
    const globals = globalThis as Record<string, any>;
    const UIViewController = api?.UIViewController ?? globals.UIViewController;
    if (!UIViewController || typeof UIViewController.alloc !== 'function') {
      throw new Error(`UIViewController is not allocatable in the UI runtime: ${typeof UIViewController}`);
    }
    const allocated = UIViewController.alloc();
    const controller = allocated && typeof allocated.init === 'function' ? allocated.init() : allocated;
    const UIColor = api?.UIColor ?? globals.UIColor;
    controller.view.backgroundColor = UIColor.clearColor;
    return controller;
  },
  childrenView(controller) {
    'worklet';
    return controller.view;
  },
  mounted(controller, props) {
    'worklet';
    const api = (globalThis as Record<string, any>).__nativeScriptNativeApi;
    const globals = globalThis as Record<string, any>;
    const UIApplication = api?.UIApplication ?? globals.UIApplication;
    const application = UIApplication?.sharedApplication;
    const windows = application?.windows;
    let root = null;
    if (windows) {
      for (let index = 0; index < windows.count; index++) {
        const window = windows.objectAtIndex(index);
        if (window.isKeyWindow) {
          root = window.rootViewController;
          break;
        }
      }
    }
    if (!root) {
      root = application?.delegate?.window?.rootViewController ?? null;
    }
    if (!root || controller.presentingViewController) {
      return;
    }
    if (props.presentation === 'overlay') {
      controller.modalPresentationStyle = 5;
    }
    root.presentViewControllerAnimatedCompletion(controller, props.presentation === 'modal', null);
  },
  dispose(controller) {
    'worklet';
    if (controller.presentingViewController) {
      controller.dismissViewControllerAnimatedCompletion(false, null);
    }
  },
});

function cloneNode(node: ParsedLayoutNode): ParsedLayoutNode {
  return {
    ...node,
    data: node.data ? {...node.data} : undefined,
    children: node.children?.map(cloneNode),
  };
}

const styles = StyleSheet.create({
  fill: {
    flex: 1,
  },
  rootFill: {
    flex: 1,
    minHeight: initialWindowHeight,
  },
});

function cloneSnapshot(snapshot: NativeScriptNavigationSnapshot): NativeScriptNavigationSnapshot {
  return {
    root: snapshot.root ? cloneNode(snapshot.root) : undefined,
    modals: snapshot.modals.map(cloneNode),
    overlays: snapshot.overlays.map(cloneNode),
  };
}

function includesLayoutId(node: ParsedLayoutNode, componentId: string): boolean {
  return node.id === componentId || (node.children ?? []).some((child) => includesLayoutId(child, componentId));
}

function mergeOptions(...options: Array<Options | undefined>): Options {
  return Object.assign({}, ...options.filter(Boolean));
}
