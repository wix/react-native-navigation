import { NativeModules } from 'react-native';

export class NativeCommandsSender {
  private readonly nativeCommandsModule;
  constructor() {
    this.nativeCommandsModule = NativeModules.RNNBridgeModule;
  }

  setRoot(commandId: string, layout: { root: any, modals: any[], overlays: any[] }) {
    return this.nativeCommandsModule.setRoot(commandId, layout);
  }

  setDefaultOptions(options: object) {
    return this.nativeCommandsModule.setDefaultOptions(options);
  }

  mergeOptions(componentId: string, options: object) {
    return this.nativeCommandsModule.mergeOptions(componentId, options);
  }

  push(commandId: string, onComponentId: string, layout: object) {
    return this.nativeCommandsModule.push(commandId, onComponentId, layout);
  }

  pop(commandId: string, componentId: string, options?: object) {
    return this.nativeCommandsModule.pop(commandId, componentId, options);
  }

  popTo(commandId: string, componentId: string, options?: object) {
    return this.nativeCommandsModule.popTo(commandId, componentId, options);
  }

  popToRoot(commandId: string, componentId: string, options?: object) {
    return this.nativeCommandsModule.popToRoot(commandId, componentId, options);
  }

  setStackRoot(commandId: string, onComponentId: string, layout: object) {
    return this.nativeCommandsModule.setStackRoot(commandId, onComponentId, layout);
  }

  showModal(commandId: string, layout: object) {
    return this.nativeCommandsModule.showModal(commandId, layout);
  }

  dismissModal(commandId: string, componentId: string, options?: object) {
    return this.nativeCommandsModule.dismissModal(commandId, componentId, options);
  }

  dismissAllModals(commandId: string, options?: object) {
    return this.nativeCommandsModule.dismissAllModals(commandId, options);
  }

  showOverlay(commandId: string, layout: object) {
    return this.nativeCommandsModule.showOverlay(commandId, layout);
  }

  dismissOverlay(commandId: string, componentId: string) {
    return this.nativeCommandsModule.dismissOverlay(commandId, componentId);
  }

  getLaunchArgs(commandId: string) {
    return this.nativeCommandsModule.getLaunchArgs(commandId);
  }
}
