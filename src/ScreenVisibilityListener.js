import {
  NativeAppEventEmitter,
  DeviceEventEmitter,
  Platform
} from 'react-native';
export default class ScreenVisibilityListener {
  constructor(listeners) {
    const emitter = Platform.OS === 'android' ? DeviceEventEmitter : NativeAppEventEmitter;
    this.listeners = listeners;
  }

  register() {
    const {willAppear, didAppear, willDisappear, didDisappear} = this.listeners;
    willAppear && emitter.addListener('willAppear', willAppear);
    didAppear && emitter.addListener('didAppear', didAppear);
    willDisappear && emitter.addListener('willDisappear', willDisappear);
    didDisappear && emitter.addListener('didDisappear', didDisappear);
  }

}
