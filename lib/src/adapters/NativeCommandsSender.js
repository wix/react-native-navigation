const { NativeModules } = require('react-native');

class NativeCommandsSender {
  constructor() {
    this.nativeCommandsModule = NativeModules.RNNBridgeModule;
  }

  setRoot(layoutTree) {
    this.nativeCommandsModule.setRoot(layoutTree);
    return Promise.resolve(layoutTree);
  }

  setOptions(containerId, options) {
    this.nativeCommandsModule.setOptions(containerId, options);
  }

  push(onContainerId, layout) {
    return this.nativeCommandsModule.push(onContainerId, layout);
  }

  pop(containerId) {
    return this.nativeCommandsModule.pop(containerId);
  }

  popTo(containerId) {
    return this.nativeCommandsModule.popTo(containerId);
  }

  popToRoot(containerId) {
    return this.nativeCommandsModule.popToRoot(containerId);
  }

  showModal(layout) {
    this.nativeCommandsModule.showModal(layout);
    return Promise.resolve(layout);
  }

  dismissModal(containerId) {
    this.nativeCommandsModule.dismissModal(containerId);
    return Promise.resolve(containerId);
  }

  dismissAllModals() {
    this.nativeCommandsModule.dismissAllModals();
    return Promise.resolve(true);
  }

  switchToTab(containerId, tabIndex) {
    this.nativeCommandsModule.switchToTab(containerId, tabIndex);
    return Promise.resolve(containerId);
  }

  showOverlay(type, options) {
    this.nativeCommandsModule.showOverlay(type, options);
    return Promise.resolve(type);
  }
}

module.exports = NativeCommandsSender;
