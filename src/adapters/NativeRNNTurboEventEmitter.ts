import { TurboModule, TurboModuleRegistry, NativeModule } from 'react-native';

export interface Spec extends TurboModule {
  addListener: (eventType: string) => void;
  removeListeners: (count: number) => void;
}

let eventEmitter: Spec | null;
eventEmitter = TurboModuleRegistry.get<Spec>('RNNTurboEventEmitter');


export default eventEmitter as NativeModule;
