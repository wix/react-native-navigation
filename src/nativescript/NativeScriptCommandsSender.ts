import { NavigationConstants } from '../adapters/Constants';
import {
  getNativeScriptNavigationStore,
  normalizeNativeScriptLayout,
} from './NativeScriptNavigationSurface';
import { NativeCommandsModule } from '../adapters/NativeCommandsSender';

type LayoutRootPayload = {
  root: any;
  modals?: any[];
  overlays?: any[];
};

export class NativeScriptCommandsSender implements NativeCommandsModule {
  private readonly store = getNativeScriptNavigationStore();

  setRoot(_commandId: string, layout: LayoutRootPayload): Promise<string> {
    const root = normalizeNativeScriptLayout(layout.root);
    const modals = (layout.modals ?? []).map(normalizeNativeScriptLayout);
    const overlays = (layout.overlays ?? []).map(normalizeNativeScriptLayout);
    this.store.setRoot(root, modals, overlays);
    return Promise.resolve(root.id ?? '');
  }

  setDefaultOptions(options: object): void {
    this.store.setDefaultOptions(options as any);
  }

  mergeOptions(componentId: string, options: object): void {
    this.store.mergeOptions(componentId, options as any);
  }

  push(_commandId: string, componentId: string, layout: object): Promise<string> {
    const node = normalizeNativeScriptLayout(layout);
    this.store.push(componentId, node);
    return Promise.resolve(node.id ?? '');
  }

  pop(_commandId: string, componentId: string, _options?: object): Promise<string> {
    return Promise.resolve(this.store.pop(componentId) ?? componentId);
  }

  popTo(_commandId: string, componentId: string, _options?: object): Promise<string> {
    return Promise.resolve(this.store.popTo(componentId) ?? componentId);
  }

  popToRoot(_commandId: string, componentId: string, _options?: object): Promise<string> {
    return Promise.resolve(this.store.popToRoot(componentId) ?? componentId);
  }

  setStackRoot(_commandId: string, componentId: string, layout: object[]): Promise<string> {
    const children = layout.map(normalizeNativeScriptLayout);
    this.store.setStackRoot(componentId, children);
    return Promise.resolve(componentId);
  }

  showModal(_commandId: string, layout: object): Promise<string> {
    const node = normalizeNativeScriptLayout(layout);
    this.store.showModal(node);
    return Promise.resolve(node.id ?? '');
  }

  dismissModal(_commandId: string, componentId: string, _options?: object): Promise<string> {
    return Promise.resolve(this.store.dismissModal(componentId) ?? componentId);
  }

  dismissAllModals(_commandId: string, _options?: object): Promise<string> {
    this.store.dismissAllModals();
    return Promise.resolve('');
  }

  showOverlay(_commandId: string, layout: object): Promise<string> {
    const node = normalizeNativeScriptLayout(layout);
    this.store.showOverlay(node);
    return Promise.resolve(node.id ?? '');
  }

  dismissOverlay(_commandId: string, componentId: string): Promise<string> {
    return Promise.resolve(this.store.dismissOverlay(componentId) ?? componentId);
  }

  dismissAllOverlays(_commandId: string): Promise<string> {
    this.store.dismissAllOverlays();
    return Promise.resolve('');
  }

  getLaunchArgs(_commandId: string): Promise<string[]> {
    return Promise.resolve([]);
  }

  getNavigationConstants(): Promise<NavigationConstants> {
    return Promise.resolve(this.getNavigationConstantsSync());
  }

  getNavigationConstantsSync(): NavigationConstants {
    return {
      topBarHeight: 44,
      statusBarHeight: 0,
      bottomTabsHeight: 49,
      backButtonId: 'RNN.back',
    };
  }
}
