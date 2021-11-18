'use strict';

import type { TurboModule } from 'react-native/Libraries/TurboModule/RCTExport.js';
import { TurboModuleRegistry } from 'react-native/Libraries/TurboModule/TurboModuleRegistry.js';

export interface NativeNavigationSpec extends TurboModule {
  getConstants: () => {};
  setRoot(
    commandId: string,
    layout: { root: any; modals: any[]; overlays: any[] }
  ): Promise<string>;
  setDefaultOptions(options: object): void;
  mergeOptions(componentId: string, options: object): Promise<string>;
  push(commandId: string, onComponentId: string, layout: object): Promise<string>;
  pop(commandId: string, componentId: string, options?: object): Promise<string>;
  popTo(commandId: string, componentId: string, options?: object): Promise<string>;
  popToRoot(commandId: string, componentId: string, options?: object): Promise<string>;
  setStackRoot(commandId: string, onComponentId: string, layout: object): Promise<string>;
  dismissModal(commandId: string, componentId: string, options?: object): Promise<string>;
  dismissAllModals(commandId: string, options?: object): Promise<string>;
  showOverlay(commandId: string, layout: object): Promise<string>;
  dismissOverlay(commandId: string, componentId: string): Promise<string>;
  dismissAllOverlays(commandId: string): Promise<string>;
}

export default TurboModuleRegistry.get<NativeNavigationSpec>('Navigation');
