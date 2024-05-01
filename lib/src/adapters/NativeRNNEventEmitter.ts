import { TurboModule, TurboModuleRegistry, NativeModules } from "react-native";

export interface Spec extends TurboModule {

}

const emitter = TurboModuleRegistry.get<Spec>("RNNEventEmitter") ?? NativeModules.RNNBridgeEventEmitter;

export default emitter;