import {
  NativeAppEventEmitter,
  DeviceEventEmitter,
  Platform
} from 'react-native';
export default class ScreenVisibilityListener {
  constructor(listeners) {
    this.emitter = Platform.OS === 'android' ? DeviceEventEmitter : NativeAppEventEmitter;
    this.listeners = listeners;
  }

  register() {
    const {willAppear, didAppear, willDisappear, didDisappear} = this.listeners;
    willAppear && this.emitter.addListener('willAppear', willAppear);
    didAppear && this.emitter.addListener('didAppear', didAppear);
    willDisappear && this.emitter.addListener('willDisappear', willDisappear);
    didDisappear && this.emitter.addListener('didDisappear', didDisappear);
  }

}
