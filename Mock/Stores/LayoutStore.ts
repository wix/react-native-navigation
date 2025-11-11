import _ from 'lodash';
import BottomTabsNode from '../Layouts/BottomTabsNode';
import ParentNode from '../Layouts/ParentNode';
import LayoutNodeFactory from '../Layouts/LayoutNodeFactory';
import { SideMenuNode } from '../Layouts/SideMenu';
import StackNode from '../Layouts/StackNode';
import { Options } from '../../src/interfaces/Options';

const remx = require('remx');

const state = remx.state({
  root: {},
  modals: [],
  overlays: [],
  sideMenu: undefined,
});

const setters = remx.setters({
  setRoot(layout: ParentNode) {
    state.modals = [];
    state.root = layout;
  },
  push(layout: ParentNode, stack: StackNode) {
    stack.children.push(layout);
  },
  pop(layoutId: string) {
    const stack = getters.getLayoutById(layoutId).getStack();
    if (stack.children.length === 1) return;
    const poppedChild = stack.children.pop();
    const newVisibleChild = stack.getVisibleLayout();
    poppedChild.componentDidDisappear();
    newVisibleChild.componentDidAppear();
    return _.clone(poppedChild.nodeId);
  },
  popTo(layoutId: string) {
    const stack = getters.getLayoutById(layoutId).getStack();
    while (stack.getVisibleLayout().nodeId != layoutId) {
      stack.children.pop();
    }
  },
  popToRoot(layoutId: string) {
    const stack = getters.getLayoutById(layoutId).getStack();
    while (stack.children.length > 1) {
      stack.children.pop();
    }
  },
  setStackRoot(layoutId: string, layout: any) {
    const currentLayout = getters.getLayoutById(layoutId);
    if (currentLayout) {
      const stack = currentLayout.getStack();
      stack.children = layout.map((child: any) => LayoutNodeFactory.create(child, stack));
    }
  },
  showOverlay(overlay: ParentNode) {
    state.overlays.push(overlay);
  },
  dismissOverlay(overlayId: string) {
    _.remove(state.overlays, (overlay: ParentNode) => overlay.nodeId === overlayId);
  },
  dismissAllOverlays() {
    state.overlays = [];
  },
  showModal(modal: ParentNode) {
    state.modals.push(modal);
  },
  dismissModal(componentId: string) {
    const modal = getters.getModalById(componentId);
    if (modal) {
      const child = modal.getVisibleLayout();
      const topParent = child.getTopParent();
      _.remove(state.modals, (modal: ParentNode) => modal.nodeId === topParent.nodeId);
    }
  },
  dismissAllModals() {
    state.modals = [];
  },
  selectTabIndex(layout: BottomTabsNode, index: number) {
    getters.getLayoutById(layout.nodeId).selectedIndex = index;
  },
  openSideMenu(layout: SideMenuNode) {
    if (state.sideMenu) {
      throw new Error(
        'A side-menu is already open; Mocked-testing of multiple side-menu scenarios is not supported yet.' +
          ' You can submit a request in https://github.com/wix/react-native-navigation/issues/new/choose.'
      );
    }
    state.sideMenu = layout;
  },
  closeSideMenu(_layout: SideMenuNode) {
    state.sideMenu = undefined;
  },
  applyOptions(componentId: string, options: Options) {
    const layout = getters.getLayoutById(componentId);
    if (layout) layout.applyOptions(options);
    else console.warn(`[RNN error] Merge options failure: cannot find layout for: ${componentId}`);
  },
  mergeOptions(componentId: string, options: Options) {
    const layout = getters.getLayoutById(componentId);
    if (layout) layout.mergeOptions(options);
    else console.warn(`[RNN error] Merge options failure: cannot find layout for: ${componentId}`);
  },
});

const getters = remx.getters({
  getLayout() {
    return state.root;
  },
  getVisibleLayout() {
    let layout: ParentNode | undefined;
    if (state.modals.length > 0) {
      layout = _.last<ParentNode>(state.modals)!;
    } else if (!_.isEqual(state.root, {})) {
      layout = state.root;
    }

    // Note: While this logic should be fair for all use cases (i.e. even multiple side-menus across tabs),
    // there is no current test case that justifies it. Nevertheless, it's required to pass the tests,
    // because otherwise getVisibleLayout() would not be revisited whenever side-menus are opened/closed.
    if (layout && state.sideMenu && findNode(state.sideMenu.nodeId, layout!)) {
      layout = state.sideMenu.parentNode;
    }

    return layout?.getVisibleLayout();
  },
  isVisibleLayout(layout: ParentNode) {
    const nodeId = layout.nodeId;
    const visibleLayout = getters.getVisibleLayout();
    return visibleLayout?.nodeId === nodeId;
  },
  getModals() {
    return state.modals;
  },
  getOverlays() {
    return state.overlays;
  },
  getLayoutById(layoutId: string) {
    if (getters.getModalById(layoutId)) return findNode(layoutId, getters.getModalById(layoutId));

    return findNode(layoutId, state.root);
  },
  getModalById(layoutId: string) {
    return _.find(state.modals, (layout) => findNode(layoutId, layout));
  },
  getLayoutChildren(layoutId: string) {
    return getters.getLayoutById(layoutId).children;
  },
  getStack(layoutId: string) {
    return (
      findStack(layoutId, state.root) ||
      _.find(state.modals, (layout) => findStack(layoutId, layout))
    );
  },
});

function findNode(layoutId: string, layout: ParentNode): any | ParentNode {
  if (layoutId === layout.nodeId) {
    return layout;
  } else if (layout.children) {
    for (let i = 0; i < layout.children.length; i += 1) {
      const child = layout.children[i];
      const result = findNode(layoutId, child);

      if (result !== false) {
        return result;
      }
    }
  }

  return false;
}

function findStack(layoutId: string, layout: ParentNode): any | ParentNode {
  if (layout.type === 'Stack' && _.find(layout.children, (child) => child.nodeId === layoutId)) {
    return layout;
  } else if (layout.children) {
    for (let i = 0; i < layout.children.length; i += 1) {
      const child = layout.children[i];
      const result = findStack(layoutId, child);

      if (result !== false) {
        return result;
      }
    }
  }

  return false;
}

let defaultOptions: Options;

export const LayoutStore = {
  ...getters,
  ...setters,
  setDefaultOptions(options: Options) {
    defaultOptions = options;
  },
  getDefaultOptions() {
    return defaultOptions;
  },
};
