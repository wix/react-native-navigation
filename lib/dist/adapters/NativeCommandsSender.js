"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const react_native_1 = require("react-native");
class NativeCommandsSender {
    constructor() {
        this.nativeCommandsModule = react_native_1.NativeModules.RNNBridgeModule;
    }
    setRoot(commandId, layout) {
        return this.nativeCommandsModule.setRoot(commandId, layout);
    }
    setDefaultOptions(options) {
        return this.nativeCommandsModule.setDefaultOptions(options);
    }
    mergeOptions(componentId, options) {
        return this.nativeCommandsModule.mergeOptions(componentId, options);
    }
    push(commandId, onComponentId, layout) {
        return this.nativeCommandsModule.push(commandId, onComponentId, layout);
    }
    pop(commandId, componentId, options) {
        return this.nativeCommandsModule.pop(commandId, componentId, options);
    }
    popTo(commandId, componentId, options) {
        return this.nativeCommandsModule.popTo(commandId, componentId, options);
    }
    popToRoot(commandId, componentId, options) {
        return this.nativeCommandsModule.popToRoot(commandId, componentId, options);
    }
    setStackRoot(commandId, onComponentId, layout) {
        return this.nativeCommandsModule.setStackRoot(commandId, onComponentId, layout);
    }
    showModal(commandId, layout) {
        return this.nativeCommandsModule.showModal(commandId, layout);
    }
    dismissModal(commandId, componentId, options) {
        return this.nativeCommandsModule.dismissModal(commandId, componentId, options);
    }
    dismissAllModals(commandId, options) {
        return this.nativeCommandsModule.dismissAllModals(commandId, options);
    }
    showOverlay(commandId, layout) {
        return this.nativeCommandsModule.showOverlay(commandId, layout);
    }
    dismissOverlay(commandId, componentId) {
        return this.nativeCommandsModule.dismissOverlay(commandId, componentId);
    }
    getLaunchArgs(commandId) {
        return this.nativeCommandsModule.getLaunchArgs(commandId);
    }
}
exports.NativeCommandsSender = NativeCommandsSender;
