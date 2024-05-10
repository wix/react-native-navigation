import { NavigationConstants } from './Constants';
import RNNCommandsModule, { RCTAssertNewArchEnabled, isRNNTurboModuleAvailable } from './NativeRNNTurboModule';

interface NativeCommandsModule {
  setRoot(commandId: string, layout: { root: any; modals: any[]; overlays: any[] }): Promise<any>;
  setDefaultOptions(options: object): void;
  mergeOptions(componentId: string, options: object): void;
  push(commandId: string, onComponentId: string, layout: object): Promise<any>;
  pop(commandId: string, componentId: string, options?: object): Promise<any>;
  popTo(commandId: string, componentId: string, options?: object): Promise<any>;
  popToRoot(commandId: string, componentId: string, options?: object): Promise<any>;
  setStackRoot(commandId: string, onComponentId: string, layout: object): Promise<any>;
  showModal(commandId: string, layout: object): Promise<any>;
  dismissModal(commandId: string, componentId: string, options?: object): Promise<any>;
  dismissAllModals(commandId: string, options?: object): Promise<any>;
  showOverlay(commandId: string, layout: object): Promise<any>;
  dismissOverlay(commandId: string, componentId: string): Promise<any>;
  dismissAllOverlays(commandId: string): Promise<any>;
  getLaunchArgs(commandId: string): Promise<any>;
  getNavigationConstants(): Promise<NavigationConstants>;
  getNavigationConstantsSync(): NavigationConstants;
  // Turbo
  getConstants?: () => NavigationConstants;
}

export class NativeCommandsSender {
  private readonly nativeCommandsModule: NativeCommandsModule;
  constructor() {
    this.nativeCommandsModule = RNNCommandsModule;
  }

  setRoot(commandId: string, layout: { root: any; modals: any[]; overlays: any[] }) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.setRootSync(commandId, layout));
    }
    return this.nativeCommandsModule.setRoot(commandId, layout);
  }

  setDefaultOptions(options: object) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.nativeCommandsModule.setDefaultOptions(options));
    }
    return this.nativeCommandsModule.setDefaultOptions(options);
  }

  mergeOptions(componentId: string, options: object) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.nativeCommandsModule.mergeOptions(componentId, options));
    }
    return this.nativeCommandsModule.mergeOptions(componentId, options);
  }

  push(commandId: string, onComponentId: string, layout: object) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.pushSync(commandId, onComponentId, layout));
    }
    return this.nativeCommandsModule.push(commandId, onComponentId, layout);
  }

  pop(commandId: string, componentId: string, options?: object) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.popSync(commandId, componentId, options));
    }
    return this.nativeCommandsModule.pop(commandId, componentId, options);
  }

  popTo(commandId: string, componentId: string, options?: object) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.popToSync(commandId, componentId, options));
    }
    return this.nativeCommandsModule.popTo(commandId, componentId, options);
  }

  popToRoot(commandId: string, componentId: string, options?: object) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.popToRootSync(commandId, componentId, options));
    }
    return this.nativeCommandsModule.popToRoot(commandId, componentId, options);
  }

  setStackRoot(commandId: string, onComponentId: string, layout: object) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.setStackRootSync(commandId, onComponentId, layout));
    }
    return this.nativeCommandsModule.setStackRoot(commandId, onComponentId, layout);
  }

  showModal(commandId: string, layout: object) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.showModalSync(commandId, layout));
    }
    return this.nativeCommandsModule.showModal(commandId, layout);
  }

  dismissModal(commandId: string, componentId: string, options?: object) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.dismissModalSync(commandId, componentId, options));
    }
    return this.nativeCommandsModule.dismissModal(commandId, componentId, options);
  }

  dismissAllModals(commandId: string, options?: object) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.dismissAllModalsSync(commandId, options));
    }
    return this.nativeCommandsModule.dismissAllModals(commandId, options);
  }

  showOverlay(commandId: string, layout: object) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.showOverlaySync(commandId, layout));
    }
    return this.nativeCommandsModule.showOverlay(commandId, layout);
  }

  dismissOverlay(commandId: string, componentId: string) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.dismissOverlaySync(commandId, componentId));
    }
    return this.nativeCommandsModule.dismissOverlay(commandId, componentId);
  }

  dismissAllOverlays(commandId: string) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.dismissAllOverlaysSync(commandId));
    }
    return this.nativeCommandsModule.dismissAllOverlays(commandId);
  }

  getLaunchArgs(commandId: string) {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.getLaunchArgsSync(commandId));
    }
    return this.nativeCommandsModule.getLaunchArgs(commandId);
  }

  getNavigationConstants() {
    if (isRNNTurboModuleAvailable) {
      return Promise.resolve(this.nativeCommandsModule.getConstants!());
    }
    return this.nativeCommandsModule.getNavigationConstants();
  }

  getNavigationConstantsSync() {
    if (isRNNTurboModuleAvailable) {
      return this.nativeCommandsModule.getConstants!();
    }
    return this.nativeCommandsModule.getNavigationConstantsSync();
  }

  // Turbo

  setRootSync(commandId: string, layout: { root: any; modals: any[]; overlays: any[] }): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.setRoot(commandId, layout);
  }

  pushSync(commandId: string, componentId: string, layout: object): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.push(commandId, componentId, layout);
  }

  popSync(commandId: string, componentId: string, options?: object): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.pop(commandId, componentId, options);
  }

  popToSync(commandId: string, componentId: string, options?: object): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.popTo(commandId, componentId, options);
  }

  popToRootSync(commandId: string, componentId: string, options?: object): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.popToRoot(commandId, componentId, options);
  }

  setStackRootSync(commandId: string, componentId: string, layout: object): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.setStackRoot(commandId, componentId, layout);
  }

  showModalSync(commandId: string, layout: object): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.showModal(commandId, layout);
  }

  dismissModalSync(commandId: string, componentId: string, options?: object): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.dismissModal(commandId, componentId, options);
  }

  dismissAllModalsSync(commandId: string, options?: object): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.dismissAllModals(commandId, options);
  }

  showOverlaySync(commandId: string, layout: object): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.showOverlay(commandId, layout);
  }

  dismissOverlaySync(commandId: string, componentId: string): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.dismissOverlay(commandId, componentId)
  }

  dismissAllOverlaysSync(commandId: string): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.dismissAllOverlays(commandId)
  }

  getLaunchArgsSync(commandId: string): any {
    RCTAssertNewArchEnabled();
    return this.nativeCommandsModule.getLaunchArgs(commandId);
  }
}
