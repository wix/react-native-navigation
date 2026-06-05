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

const STACK_TRANSITION_LOCK_MS = 360;

export class NativeScriptCommandsSender implements NativeCommandsModule {
  private readonly store = getNativeScriptNavigationStore();
  private readonly pendingStackCommands = new Map<string, Promise<string>>();

  private resolveAfterStackTransition(stackId: string, result: string): Promise<string> {
    const pending = new Promise<string>((resolve) => {
      setTimeout(() => {
        if (this.pendingStackCommands.get(stackId) === pending) {
          this.pendingStackCommands.delete(stackId);
        }
        resolve(result);
      }, STACK_TRANSITION_LOCK_MS);
    });
    this.pendingStackCommands.set(stackId, pending);
    return pending;
  }

  private runStackCommand(
    componentId: string,
    fallbackResult: string,
    command: () => {didMutate: boolean; result: string; stackId?: string}
  ): Promise<string> {
    const candidateStackId = this.store.getStackIdForComponent(componentId) ?? componentId;
    const pending = this.pendingStackCommands.get(candidateStackId);
    if (pending) {
      return pending;
    }

    const mutation = command();
    if (!mutation.didMutate) {
      return Promise.resolve(fallbackResult);
    }

    return this.resolveAfterStackTransition(
      mutation.stackId ?? candidateStackId,
      mutation.result
    );
  }

  setRoot(_commandId: string, layout: LayoutRootPayload): Promise<string> {
    this.pendingStackCommands.clear();
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
    const pushedId = node.id ?? '';
    return this.runStackCommand(componentId, pushedId, () => {
      const mutation = this.store.push(componentId, node);
      return {
        didMutate: mutation.didPush,
        result: mutation.pushedId ?? pushedId,
        stackId: mutation.stackId,
      };
    });
  }

  pop(_commandId: string, componentId: string, _options?: object): Promise<string> {
    return this.runStackCommand(componentId, componentId, () => {
      const stackId = this.store.getStackIdForComponent(componentId);
      const removed = this.store.pop(componentId);
      return {
        didMutate: removed != null,
        result: removed ?? componentId,
        stackId,
      };
    });
  }

  popTo(_commandId: string, componentId: string, _options?: object): Promise<string> {
    return this.runStackCommand(componentId, componentId, () => {
      const stackId = this.store.getStackIdForComponent(componentId);
      const removed = this.store.popTo(componentId);
      return {
        didMutate: removed != null,
        result: removed ?? componentId,
        stackId,
      };
    });
  }

  popToRoot(_commandId: string, componentId: string, _options?: object): Promise<string> {
    return this.runStackCommand(componentId, componentId, () => {
      const stackId = this.store.getStackIdForComponent(componentId);
      const removed = this.store.popToRoot(componentId);
      return {
        didMutate: removed != null,
        result: removed ?? componentId,
        stackId,
      };
    });
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
