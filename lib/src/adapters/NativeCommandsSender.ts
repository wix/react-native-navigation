import { NativeModules } from 'react-native';

export class NativeCommandsSender {
  private nativeCommandsModule;
  constructor() {
    this.nativeCommandsModule = NativeModules.RNNBridgeModule;
  }

  setRoot(layoutTree: object) {
    return this.nativeCommandsModule.setRoot(layoutTree);
  }

  setDefaultOptions(options: object) {
    return this.nativeCommandsModule.setDefaultOptions(options);
  }

  setOptions(componentId: string, options: object) {
    return this.nativeCommandsModule.setOptions(componentId, options);
  }

  push(onComponentId: string, layout: object) {
    return this.nativeCommandsModule.push(onComponentId, layout);
  }

  pop(componentId: string, options: object) {
    return this.nativeCommandsModule.pop(componentId, options);
  }

  popTo(componentId: string) {
    return this.nativeCommandsModule.popTo(componentId);
  }

  popToRoot(componentId: string) {
    return this.nativeCommandsModule.popToRoot(componentId);
  }

  showModal(layout: object) {
    return this.nativeCommandsModule.showModal(layout);
  }

  dismissModal(componentId: string) {
    return this.nativeCommandsModule.dismissModal(componentId);
  }

  dismissAllModals() {
    return this.nativeCommandsModule.dismissAllModals();
  }

  showOverlay(layout: object) {
    return this.nativeCommandsModule.showOverlay(layout);
  }

  dismissOverlay(componentId: string) {
    return this.nativeCommandsModule.dismissOverlay(componentId);
  }
}
