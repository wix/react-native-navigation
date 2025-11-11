"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.NativeCommandsSender = void 0;
const tslib_1 = require("tslib");
const LayoutStore_1 = require("../Stores/LayoutStore");
const LayoutNodeFactory_1 = tslib_1.__importDefault(require("../Layouts/LayoutNodeFactory"));
const EventsStore_1 = require("../Stores/EventsStore");
const lodash_1 = tslib_1.__importDefault(require("lodash"));
const CommandName_1 = require("../../src/interfaces/CommandName");
class NativeCommandsSender {
    constructor() { }
    setRoot(commandId, layout) {
        return new Promise((resolve) => {
            if (LayoutStore_1.LayoutStore.getVisibleLayout()) {
                LayoutStore_1.LayoutStore.getVisibleLayout().componentDidDisappear();
                LayoutStore_1.LayoutStore.setRoot({});
            }
            const layoutNode = LayoutNodeFactory_1.default.create(layout.root);
            LayoutStore_1.LayoutStore.setRoot(layoutNode);
            layoutNode.getVisibleLayout().componentDidAppear();
            resolve(layout.root.nodeId);
            this.reportCommandCompletion(CommandName_1.CommandName.SetRoot, commandId);
        });
    }
    setDefaultOptions(options) {
        LayoutStore_1.LayoutStore.setDefaultOptions(options);
    }
    mergeOptions(componentId, options) {
        LayoutStore_1.LayoutStore.mergeOptions(componentId, options);
    }
    push(commandId, onComponentId, layout) {
        return new Promise((resolve) => {
            const stack = LayoutStore_1.LayoutStore.getLayoutById(onComponentId).getStack();
            const layoutNode = LayoutNodeFactory_1.default.create(layout, stack);
            stack.getVisibleLayout().componentDidDisappear();
            LayoutStore_1.LayoutStore.push(layoutNode, stack);
            LayoutStore_1.LayoutStore.applyOptions(layoutNode.nodeId, layoutNode.data.options);
            stack.getVisibleLayout().componentDidAppear();
            resolve(stack.getVisibleLayout().nodeId);
            this.reportCommandCompletion(CommandName_1.CommandName.Push, commandId);
        });
    }
    pop(commandId, componentId, _options) {
        return new Promise((resolve, reject) => {
            if (!LayoutStore_1.LayoutStore.getLayoutById(componentId)) {
                reject(`Popping component failed - componentId '${componentId}' not found`);
                return;
            }
            const poppedChild = lodash_1.default.last(LayoutStore_1.LayoutStore.getLayoutById(componentId).getStack().children);
            LayoutStore_1.LayoutStore.pop(componentId);
            EventsStore_1.events.invokeScreenPopped({
                componentId,
            });
            resolve(poppedChild.nodeId);
            this.reportCommandCompletion(CommandName_1.CommandName.Pop, commandId);
        });
    }
    popTo(commandId, componentId, _options) {
        return new Promise((resolve) => {
            LayoutStore_1.LayoutStore.popTo(componentId);
            resolve(componentId);
            this.reportCommandCompletion(CommandName_1.CommandName.PopTo, commandId);
        });
    }
    popToRoot(commandId, componentId, _options) {
        LayoutStore_1.LayoutStore.popToRoot(componentId);
        this.reportCommandCompletion(CommandName_1.CommandName.PopToRoot, commandId);
    }
    setStackRoot(commandId, onComponentId, layout) {
        LayoutStore_1.LayoutStore.setStackRoot(onComponentId, layout);
        this.reportCommandCompletion(CommandName_1.CommandName.SetStackRoot, commandId);
    }
    showModal(commandId, layout) {
        return new Promise((resolve) => {
            const layoutNode = LayoutNodeFactory_1.default.create(layout);
            LayoutStore_1.LayoutStore.getVisibleLayout().componentDidDisappear();
            LayoutStore_1.LayoutStore.showModal(layoutNode);
            layoutNode.componentDidAppear();
            resolve(layoutNode.nodeId);
            this.reportCommandCompletion(CommandName_1.CommandName.ShowModal, commandId);
        });
    }
    dismissModal(commandId, componentId, _options) {
        return new Promise((resolve, reject) => {
            const modal = LayoutStore_1.LayoutStore.getModalById(componentId);
            if (modal) {
                const modalTopParent = modal.getTopParent();
                modalTopParent.componentDidDisappear();
                LayoutStore_1.LayoutStore.dismissModal(componentId);
                EventsStore_1.events.invokeModalDismissed({
                    componentName: modalTopParent.data.name,
                    componentId: modalTopParent.nodeId,
                    modalsDismissed: 1,
                });
                resolve(modalTopParent.nodeId);
                LayoutStore_1.LayoutStore.getVisibleLayout().componentDidAppear();
                this.reportCommandCompletion(CommandName_1.CommandName.DismissModal, commandId);
            }
            else {
                reject(`component with id: ${componentId} is not a modal`);
            }
        });
    }
    dismissAllModals(commandId, _options) {
        LayoutStore_1.LayoutStore.dismissAllModals();
        this.reportCommandCompletion(CommandName_1.CommandName.DismissAllModals, commandId);
    }
    showOverlay(commandId, layout) {
        const layoutNode = LayoutNodeFactory_1.default.create(layout);
        LayoutStore_1.LayoutStore.showOverlay(layoutNode);
        layoutNode.componentDidAppear();
        this.reportCommandCompletion(CommandName_1.CommandName.ShowOverlay, commandId);
    }
    dismissOverlay(commandId, componentId) {
        LayoutStore_1.LayoutStore.dismissOverlay(componentId);
        this.reportCommandCompletion(CommandName_1.CommandName.DismissOverlay, commandId);
    }
    dismissAllOverlays(commandId) {
        LayoutStore_1.LayoutStore.dismissAllOverlays();
        this.reportCommandCompletion(CommandName_1.CommandName.DismissAllOverlays, commandId);
    }
    getLaunchArgs(commandId) {
        this.reportCommandCompletion(CommandName_1.CommandName.GetLaunchArgs, commandId);
    }
    getNavigationConstants() {
        return Promise.resolve({
            topBarHeight: 0,
            backButtonId: 'RNN.back',
            bottomTabsHeight: 0,
            statusBarHeight: 0,
        });
    }
    getNavigationConstantsSync() {
        return {
            topBarHeight: 0,
            backButtonId: 'RNN.back',
            bottomTabsHeight: 0,
            statusBarHeight: 0,
        };
    }
    reportCommandCompletion(commandName, commandId) {
        EventsStore_1.events.invokeCommandCompleted({
            commandName,
            commandId,
            completionTime: 0,
        });
    }
}
exports.NativeCommandsSender = NativeCommandsSender;
