const { NativeModules } = require('react-native');

class NativeCommandsSender {
  constructor() {
    this.nativeCommandsModule = NativeModules.RNNBridgeModule;
  }

  setRoot(layoutTree) {
    return this.nativeCommandsModule.setRoot(layoutTree);
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
    return this.nativeCommandsModule.showModal(layout);
  }

  dismissModal(containerId) {
    return this.nativeCommandsModule.dismissModal(containerId);
  }

  dismissAllModals() {
    return this.nativeCommandsModule.dismissAllModals();
  }

  switchToTab(containerId, tabIndex) {
    this.nativeCommandsModule.switchToTab(containerId, tabIndex);
    return Promise.resolve(containerId);
  }

  showOverlay(type, options) {
    return this.nativeCommandsModule.showOverlay(type, options);
  }
}

module.exports = NativeCommandsSender;
