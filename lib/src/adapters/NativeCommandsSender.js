const { NativeModules } = require('react-native');

class NativeCommandsSender {
  constructor() {
    this.nativeCommandsModule = NativeModules.RNNBridgeModule;
  }

  setRoot(layoutTree) {
    return this.nativeCommandsModule.setRoot(layoutTree);
  }

  setDefaultOptions(options) {
    this.nativeCommandsModule.setDefaultOptions(options);
  }

  setOptions(componentId, options) {
    this.nativeCommandsModule.setOptions(componentId, options);
  }

  async push(onComponentId, layout) {
    const pushedComponentId = await this.nativeCommandsModule.push(onComponentId, layout);
    return pushedComponentId;
  }

  pop(componentId, options) {
    return this.nativeCommandsModule.pop(componentId, options);
  }

  popTo(componentId) {
    return this.nativeCommandsModule.popTo(componentId);
  }

  popToRoot(componentId) {
    return this.nativeCommandsModule.popToRoot(componentId);
  }

  async showModal(layout) {
    const completed = await this.nativeCommandsModule.showModal(layout);
    return completed;
  }

  dismissModal(componentId) {
    return this.nativeCommandsModule.dismissModal(componentId);
  }

  dismissAllModals() {
    return this.nativeCommandsModule.dismissAllModals();
  }

  showOverlay(layout) {
    return this.nativeCommandsModule.showOverlay(layout);
  }

  dismissOverlay(componentId) {
    return this.nativeCommandsModule.dismissOverlay(componentId);
  }
}

module.exports = NativeCommandsSender;
