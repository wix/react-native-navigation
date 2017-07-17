import {
  NativeAppEventEmitter,
  DeviceEventEmitter,
  Platform
} from 'react-native';
export default class ScreenVisibilityListener {
  constructor({willAppear, didAppear, willDisappear, didDisappear}) {
    const emitter = Platform.OS === 'android' ? DeviceEventEmitter : NativeAppEventEmitter;
    willAppear && emitter.addListener('willAppear', willAppear);
    didAppear && emitter.addListener('didAppear', didAppear);
    willDisappear && emitter.addListener('willDisappear', willDisappear);
    didDisappear && emitter.addListener('didDisappear', didDisappear);
  }
}
