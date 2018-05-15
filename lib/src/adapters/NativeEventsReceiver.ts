import { NativeModules, NativeEventEmitter } from 'react-native';
import { EventSubscription } from '../interfaces/EventSubscription';

export class NativeEventsReceiver {
  private emitter: NativeEventEmitter;
  constructor() {
    this.emitter = new NativeEventEmitter(NativeModules.RNNEventEmitter);
  }

  public registerAppLaunchedListener(callback: () => void): EventSubscription {
    return this.emitter.addListener('RNN.appLaunched', callback);
  }

  public registerComponentDidAppearListener(callback: (data) => void): EventSubscription {
    return this.emitter.addListener('RNN.componentDidAppear', callback);
  }

  public registerComponentDidDisappearListener(callback: (data) => void): EventSubscription {
    return this.emitter.addListener('RNN.componentDidDisappear', callback);
  }

  public registerCommandCompletedListener(callback: (data) => void): EventSubscription {
    return this.emitter.addListener('RNN.commandCompleted', callback);
  }

  public registerNativeEventListener(callback: (data) => void): EventSubscription {
    return this.emitter.addListener('RNN.nativeEvent', callback);
  }
}
