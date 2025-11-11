import { LayoutNode } from '../../src/commands/LayoutTreeCrawler';
import { Constants } from '../../src/adapters/Constants';
export declare class NativeCommandsSender {
    constructor();
    setRoot(commandId: string, layout: {
        root: any;
        modals: any[];
        overlays: any[];
    }): Promise<unknown>;
    setDefaultOptions(options: object): void;
    mergeOptions(componentId: string, options: object): void;
    push(commandId: string, onComponentId: string, layout: LayoutNode): Promise<unknown>;
    pop(commandId: string, componentId: string, _options?: object): Promise<unknown>;
    popTo(commandId: string, componentId: string, _options?: object): Promise<unknown>;
    popToRoot(commandId: string, componentId: string, _options?: object): void;
    setStackRoot(commandId: string, onComponentId: string, layout: object): void;
    showModal(commandId: string, layout: object): Promise<unknown>;
    dismissModal(commandId: string, componentId: string, _options?: object): Promise<unknown>;
    dismissAllModals(commandId: string, _options?: object): void;
    showOverlay(commandId: string, layout: object): void;
    dismissOverlay(commandId: string, componentId: string): void;
    dismissAllOverlays(commandId: string): void;
    getLaunchArgs(commandId: string): void;
    getNavigationConstants(): Promise<Constants>;
    getNavigationConstantsSync(): Constants;
    private reportCommandCompletion;
}
