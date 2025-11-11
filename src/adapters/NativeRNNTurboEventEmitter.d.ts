import { TurboModule, NativeModule } from 'react-native';
export interface Spec extends TurboModule {
    addListener: (eventType: string) => void;
    removeListeners: (count: number) => void;
}
declare const _default: NativeModule;
export default _default;
