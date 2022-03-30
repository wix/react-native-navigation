import _ from 'lodash';
import BottomTabsNode from '../Layouts/BottomTabsNode';
import ParentNode from '../Layouts/ParentNode';
import LayoutNodeFactory from '../Layouts/LayoutNodeFactory';
import { Options } from '../../src/interfaces/Options';
import StackNode from '../Layouts/StackNode';

const remx = require('remx');

const state = remx.state({
  root: {},
  modals: [],
  overlays: [],
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
    if (state.modals.length > 0) {
      return _.last<ParentNode>(state.modals)!.getVisibleLayout();
    } else if (!_.isEqual(state.root, {})) return state.root.getVisibleLayout();
  },
  isVisibleLayout(layout: ParentNode) {
    return getters.getVisibleLayout() && getters.getVisibleLayout().nodeId === layout.nodeId;
  },
  getModals() {
    return state.modals;
  },
  getOverlays() {
    return state.overlays;
  },
  getLayoutById(layoutId: string) {
    if (getters.getModalById(layoutId))
      return findParentNode(layoutId, getters.getModalById(layoutId));

    return findParentNode(layoutId, state.root);
  },
  getModalById(layoutId: string) {
    return _.find(state.modals, (layout) => findParentNode(layoutId, layout));
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

function findParentNode(layoutId: string, layout: ParentNode): any | ParentNode {
  if (layoutId === layout.nodeId) {
    return layout;
  } else if (layout.children) {
    for (let i = 0; i < layout.children.length; i += 1) {
      const child = layout.children[i];
      const result = findParentNode(layoutId, child);

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
