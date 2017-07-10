import { NativeModules } from 'react-native';

export default class NativeCommandsSender {
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
    this.nativeCommandsModule.push(onContainerId, layout);
    return Promise.resolve(layout);
  }

  pop(containerId) {
    this.nativeCommandsModule.pop(containerId);
    return Promise.resolve(containerId);
  }

  popTo(containerId) {
    this.nativeCommandsModule.popTo(containerId);
    return Promise.resolve(containerId);
  }

  popToRoot(containerId) {
    this.nativeCommandsModule.popToRoot(containerId);
    return Promise.resolve(containerId);
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
}

