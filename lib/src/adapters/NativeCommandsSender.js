const { NativeModules } = require('react-native');

class NativeCommandsSender {
  constructor() {
    this.nativeCommandsModule = NativeModules.RNNBridgeModule;
  }

  async setRoot(layoutTree) {
    const layout = await this.nativeCommandsModule.setRoot(layoutTree);
    return layout;
  }

  setOptions(containerId, options) {
    this.nativeCommandsModule.setOptions(containerId, options);
  }

  async push(onContainerId, layout) {
    const pushedContainerId = await this.nativeCommandsModule.push(onContainerId, layout);
    return pushedContainerId;
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

  async showModal(layout) {
    const completed = await this.nativeCommandsModule.showModal(layout);
    return completed;
  }

  dismissModal(containerId) {
    return this.nativeCommandsModule.dismissModal(containerId);
  }

  dismissAllModals() {
    return this.nativeCommandsModule.dismissAllModals();
  }

  showOverlay(type, options) {
    return this.nativeCommandsModule.showOverlay(type, options);
  }

  dismissOverlay() {
    this.nativeCommandsModule.dismissOverlay();
    return Promise.resolve(true);
  }
}

module.exports = NativeCommandsSender;
