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
    this.willAppearSubscription = willAppear && this.emitter.addListener('willAppear', willAppear);
    this.didAppearSubscription = didAppear && this.emitter.addListener('didAppear', didAppear);
    this.willDisappearSubscription = willDisappear && this.emitter.addListener('willDisappear', willDisappear);
    this.didDisappearSubscription  = didDisappear && this.emitter.addListener('didDisappear', didDisappear);
  }

  unregister() {
    this.willAppearSubscription && this.willAppearSubscription.remove();
    this.didAppearSubscription && this.didAppearSubscription.remove();
    this.willDisappearSubscription && this.willDisappearSubscription.remove();
    this.didDisappearSubscription && this.didDisappearSubscription.remove();
  }
}
