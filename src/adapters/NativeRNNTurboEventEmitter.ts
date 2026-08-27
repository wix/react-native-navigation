import { TurboModule, TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  addListener: (eventType: string) => void;
  removeListeners: (count: number) => void;
}

const eventEmitter = TurboModuleRegistry.get<Spec>('RNNTurboEventEmitter');


export default eventEmitter;
