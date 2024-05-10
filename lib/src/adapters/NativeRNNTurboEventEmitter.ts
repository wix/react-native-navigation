import { TurboModule, TurboModuleRegistry, NativeModules, NativeModule } from "react-native";

export interface Spec extends TurboModule {
    addListener: (eventType: string) => void;
    removeListeners: (count: number) => void;
}

let eventEmitter: Spec | null = null;

try {
    // Running in bridge mode
    eventEmitter = NativeModules.RNNBridgeEventEmitter;
} catch (e) {
    // Running in bridgeless, access to NativeModules is prohibited
    eventEmitter = TurboModuleRegistry.get<Spec>("RNNTurboEventEmitter");
}

export default eventEmitter as NativeModule;