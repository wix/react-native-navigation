"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.LayoutStore = void 0;
const tslib_1 = require("tslib");
const lodash_1 = tslib_1.__importDefault(require("lodash"));
const LayoutNodeFactory_1 = tslib_1.__importDefault(require("../Layouts/LayoutNodeFactory"));
const remx = require('remx');
const state = remx.state({
    root: {},
    modals: [],
    overlays: [],
    sideMenu: undefined,
});
const setters = remx.setters({
    setRoot(layout) {
        state.modals = [];
        state.root = layout;
    },
    push(layout, stack) {
        stack.children.push(layout);
    },
    pop(layoutId) {
        const stack = getters.getLayoutById(layoutId).getStack();
        if (stack.children.length === 1)
            return;
        const poppedChild = stack.children.pop();
        const newVisibleChild = stack.getVisibleLayout();
        poppedChild.componentDidDisappear();
        newVisibleChild.componentDidAppear();
        return lodash_1.default.clone(poppedChild.nodeId);
    },
    popTo(layoutId) {
        const stack = getters.getLayoutById(layoutId).getStack();
        while (stack.getVisibleLayout().nodeId != layoutId) {
            stack.children.pop();
        }
    },
    popToRoot(layoutId) {
        const stack = getters.getLayoutById(layoutId).getStack();
        while (stack.children.length > 1) {
            stack.children.pop();
        }
    },
    setStackRoot(layoutId, layout) {
        const currentLayout = getters.getLayoutById(layoutId);
        if (currentLayout) {
            const stack = currentLayout.getStack();
            stack.children = layout.map((child) => LayoutNodeFactory_1.default.create(child, stack));
        }
    },
    showOverlay(overlay) {
        state.overlays.push(overlay);
    },
    dismissOverlay(overlayId) {
        lodash_1.default.remove(state.overlays, (overlay) => overlay.nodeId === overlayId);
    },
    dismissAllOverlays() {
        state.overlays = [];
    },
    showModal(modal) {
        state.modals.push(modal);
    },
    dismissModal(componentId) {
        const modal = getters.getModalById(componentId);
        if (modal) {
            const child = modal.getVisibleLayout();
            const topParent = child.getTopParent();
            lodash_1.default.remove(state.modals, (modal) => modal.nodeId === topParent.nodeId);
        }
    },
    dismissAllModals() {
        state.modals = [];
    },
    selectTabIndex(layout, index) {
        getters.getLayoutById(layout.nodeId).selectedIndex = index;
    },
    openSideMenu(layout) {
        if (state.sideMenu) {
            throw new Error('A side-menu is already open; Mocked-testing of multiple side-menu scenarios is not supported yet.' +
                ' You can submit a request in https://github.com/wix/react-native-navigation/issues/new/choose.');
        }
        state.sideMenu = layout;
    },
    closeSideMenu(_layout) {
        state.sideMenu = undefined;
    },
    applyOptions(componentId, options) {
        const layout = getters.getLayoutById(componentId);
        if (layout)
            layout.applyOptions(options);
        else
            console.warn(`[RNN error] Merge options failure: cannot find layout for: ${componentId}`);
    },
    mergeOptions(componentId, options) {
        const layout = getters.getLayoutById(componentId);
        if (layout)
            layout.mergeOptions(options);
        else
            console.warn(`[RNN error] Merge options failure: cannot find layout for: ${componentId}`);
    },
});
const getters = remx.getters({
    getLayout() {
        return state.root;
    },
    getVisibleLayout() {
        let layout;
        if (state.modals.length > 0) {
            layout = lodash_1.default.last(state.modals);
        }
        else if (!lodash_1.default.isEqual(state.root, {})) {
            layout = state.root;
        }
        // Note: While this logic should be fair for all use cases (i.e. even multiple side-menus across tabs),
        // there is no current test case that justifies it. Nevertheless, it's required to pass the tests,
        // because otherwise getVisibleLayout() would not be revisited whenever side-menus are opened/closed.
        if (layout && state.sideMenu && findNode(state.sideMenu.nodeId, layout)) {
            layout = state.sideMenu.parentNode;
        }
        return layout?.getVisibleLayout();
    },
    isVisibleLayout(layout) {
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
    getLayoutById(layoutId) {
        if (getters.getModalById(layoutId))
            return findNode(layoutId, getters.getModalById(layoutId));
        return findNode(layoutId, state.root);
    },
    getModalById(layoutId) {
        return lodash_1.default.find(state.modals, (layout) => findNode(layoutId, layout));
    },
    getLayoutChildren(layoutId) {
        return getters.getLayoutById(layoutId).children;
    },
    getStack(layoutId) {
        return (findStack(layoutId, state.root) ||
            lodash_1.default.find(state.modals, (layout) => findStack(layoutId, layout)));
    },
});
function findNode(layoutId, layout) {
    if (layoutId === layout.nodeId) {
        return layout;
    }
    else if (layout.children) {
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
function findStack(layoutId, layout) {
    if (layout.type === 'Stack' && lodash_1.default.find(layout.children, (child) => child.nodeId === layoutId)) {
        return layout;
    }
    else if (layout.children) {
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
let defaultOptions;
exports.LayoutStore = {
    ...getters,
    ...setters,
    setDefaultOptions(options) {
        defaultOptions = options;
    },
    getDefaultOptions() {
        return defaultOptions;
    },
};
