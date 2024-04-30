import { TurboModule, TurboModuleRegistry, NativeModules } from "react-native";
import { UnsafeObject, Double } from "react-native/Libraries/Types/CodegenTypes";

export interface Spec extends TurboModule {
    readonly getConstants: () => {
        topBarHeight: Double;
        statusBarHeight: Double;
        bottomTabsHeight: Double;
    };

    setRoot(commandId: string, layout: UnsafeObject): string;
    setDefaultOptions(options: UnsafeObject): void;
    mergeOptions(componentId: string, options: UnsafeObject): string;
    push(commandId: string, onComponentId: string, layout: UnsafeObject): string;
    pop(commandId: string, componentId: string, options: UnsafeObject): string;
    popTo(commandId: string, componentId: string, options: UnsafeObject): string;
    popToRoot(commandId: string, componentId: string, options: UnsafeObject): string;
    setStackRoot(commandId: string, onComponentId: string, layout: UnsafeObject): string;
    showModal(commandId: string, layout: UnsafeObject): string;
    dismissModal(commandId: string, componentId: string, options: UnsafeObject): void;
    dismissAllModals(commandId: string, options: UnsafeObject): void;
    showOverlay(commandId: string, layout: UnsafeObject): string;
    dismissOverlay(commandId: string, componentId: string): void;
    dismissAllOverlays(commandId: string): void;
    getLaunchArgs(commandId: string): Array<string>;
}

const commands = TurboModuleRegistry.get<Spec>("RNNCommandsModule") ?? NativeModules.RNNBridgeModule;

export default commands;