import { TurboModule, TurboModuleRegistry, NativeModules } from "react-native";

export interface Spec extends TurboModule {
    addListener: (eventType: string) => void;
    removeListeners: (count: number) => void;
}

const emitter = TurboModuleRegistry.get<Spec>("RNNEventEmitter") ?? NativeModules.RNNEventEmitter;

export default emitter;