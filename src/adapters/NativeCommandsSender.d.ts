import { NavigationConstants } from './Constants';
interface NativeCommandsModule {
    setRoot(commandId: string, layout: {
        root: any;
        modals: any[];
        overlays: any[];
    }): Promise<any>;
    setDefaultOptions(options: object): void;
    mergeOptions(componentId: string, options: object): void;
    push(commandId: string, onComponentId: string, layout: object): Promise<any>;
    pop(commandId: string, componentId: string, options?: object): Promise<any>;
    popTo(commandId: string, componentId: string, options?: object): Promise<any>;
    popToRoot(commandId: string, componentId: string, options?: object): Promise<any>;
    setStackRoot(commandId: string, onComponentId: string, layout: object[]): Promise<any>;
    showModal(commandId: string, layout: object): Promise<any>;
    dismissModal(commandId: string, componentId: string, options?: object): Promise<any>;
    dismissAllModals(commandId: string, options?: object): Promise<any>;
    showOverlay(commandId: string, layout: object): Promise<any>;
    dismissOverlay(commandId: string, componentId: string): Promise<any>;
    dismissAllOverlays(commandId: string): Promise<any>;
    getLaunchArgs(commandId: string): Promise<any>;
    getNavigationConstants(): Promise<NavigationConstants>;
    getNavigationConstantsSync(): NavigationConstants;
    getConstants?: () => NavigationConstants;
}
export declare class NativeCommandsSender implements NativeCommandsModule {
    private readonly nativeCommandsModule;
    constructor();
    setRoot(commandId: string, layout: {
        root: any;
        modals: any[];
        overlays: any[];
    }): Promise<string>;
    setDefaultOptions(options: object): void;
    mergeOptions(componentId: string, options: object): void;
    push(commandId: string, onComponentId: string, layout: object): Promise<string>;
    pop(commandId: string, componentId: string, options?: object): Promise<string>;
    popTo(commandId: string, componentId: string, options?: object): Promise<string>;
    popToRoot(commandId: string, componentId: string, options?: object): Promise<string>;
    setStackRoot(commandId: string, onComponentId: string, layout: object[]): Promise<string>;
    showModal(commandId: string, layout: object): Promise<string>;
    dismissModal(commandId: string, componentId: string, options?: object): Promise<string>;
    dismissAllModals(commandId: string, options?: object): Promise<string>;
    showOverlay(commandId: string, layout: object): Promise<string>;
    dismissOverlay(commandId: string, componentId: string): Promise<string>;
    dismissAllOverlays(commandId: string): Promise<string>;
    getLaunchArgs(commandId: string): Promise<string[]>;
    getNavigationConstants(): Promise<{
        topBarHeight: number;
        statusBarHeight: number;
        bottomTabsHeight: number;
        backButtonId: string;
    }>;
    getNavigationConstantsSync(): {
        topBarHeight: number;
        statusBarHeight: number;
        bottomTabsHeight: number;
        backButtonId: string;
    };
}
export {};
