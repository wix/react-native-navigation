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
    this.nativeCommandsModule.setDefaultOptions(options);
  }

  setOptions(componentId: string, options: object) {
    this.nativeCommandsModule.setOptions(componentId, options);
  }

  async push(onComponentId: string, layout: object) {
    const pushedComponentId = await this.nativeCommandsModule.push(onComponentId, layout);
    return pushedComponentId;
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

  async showModal(layout: object) {
    const completed = await this.nativeCommandsModule.showModal(layout);
    return completed;
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
